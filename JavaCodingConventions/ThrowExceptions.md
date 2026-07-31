## Exception-Wurf im Detail

**1. Objekterzeugung + Stacktrace-Capture**

Bei `throw new FooException()` läuft normale Objektallokation, danach ruft der `Throwable`-Konstruktor die native Methode `fillInStackTrace()`. Die läuft synchron im werfenden Thread selbst (kein Safepoint nötig, da Self-Inspection des eigenen Stacks) und legt eine kompakte interne Backtrace-Struktur an – **nicht** schon das fertige `StackTraceElement[]`. Das Array wird erst lazy bei `getStackTrace()`/`printStackTrace()` materialisiert.

Bei JVM-intern erzeugten Exceptions (NPE, `ArithmeticException`, `ArrayIndexOutOfBoundsException`, `ClassCastException`) gibt es eine Optimierung: `-XX:+OmitStackTraceInFastThrow` (default an). Wenn dieselbe implizite Exception an derselben Bytecode-Stelle wiederholt (hot) geworfen wird, ersetzt HotSpot die Allokation+Stacktrace-Füllung durch eine vorallozierte, stacktrace-lose Instanz ("...<no stack trace available>"). Das ist der klassische Grund, warum Exceptions als Kontrollfluss in Schleifen irgendwann plötzlich ohne Stacktrace auftauchen.

**2. `athrow` und Stack-Unwinding**

Die Bytecode-Instruktion `athrow` popt die Referenz und startet die Handler-Suche: JVM (Interpreter oder kompilierter Code über die Exception-Table/OopMaps der Methode) prüft, ob der aktuelle Frame einen passenden Handler hat (PC-Range + Typcheck via Klassenhierarchie). Kein Treffer → Frame wird gepoppt, dabei:
- `monitorexit` für alle aktiven `synchronized`-Blöcke/-Methoden dieses Frames (Lock-Freigabe)
- Lokale Variablen werden GC-fähig
- Suche geht rekursiv im Caller-Frame weiter

`finally` existiert seit Java 6 nicht mehr als `jsr/ret`, sondern wird vom Compiler als dupliziertes Inline-Bytecode an jedem Exit-Pfad erzeugt plus einem "catch-all"-Handlereintrag in der Exception-Table.

Kein Handler auf dem ganzen Stack → `Thread.dispatchUncaughtException` → `UncaughtExceptionHandler`.

## JIT / Deoptimierung

Hier liegt der eigentlich teure Teil, wenn man's falsch macht:

- **C2 profiliert Exception-Pfade.** Wird ein `catch`-Zweig beim Profiling nie oder selten genommen, kompiliert C2 ihn oft gar nicht mit ein, sondern legt dort einen **Uncommon Trap** ab. Wird der Pfad dann zur Laufzeit doch getroffen, löst das eine **Deoptimierung genau dieser Methode** aus: Rücksprung in den Interpreter für diesen Frame (`deoptimize`), Weiterlaufen interpretiert.
- Ob die Methode danach neu kompiliert wird, hängt von der Trap-Historie ab (`PerMethodTrapLimit`/`PerBytecodeTrapLimit`). Wird der Trap zu oft ausgelöst, markiert HotSpot die Methode dauerhaft als "nicht mehr kompilierbar für diesen Pfad" – sie bleibt interpretiert/schlechter optimiert.
- Reines Werfen/Fangen von Exceptions durch bereits kompilierten Code, der den Handler von Anfang an korrekt einkompiliert hat, verursacht **keine** Deoptimierung – Unwinding durch kompilierte Frames läuft über generierte Runtime-Stubs, GC-sicher via OopMaps, ohne Rückfall in den Interpreter.

Kurzform: Deopt passiert nicht wegen des Werfens an sich, sondern wenn die Exception einen zur Compile-Zeit als "kalt" eingestuften Pfad tatsächlich aktiviert.

## Auswirkung auf eine Tomcat-Webanwendung

- **Stacktrace-Kosten skalieren mit Stacktiefe.** Bei Spring/Hibernate-Stacks mit 60–150+ Frames pro Request ist `fillInStackTrace()` bei hoher Exception-Rate (z. B. Validierungsexceptions als Kontrollfluss) ein messbarer CPU-Kostenfaktor.
- **Allokationsdruck:** Jede Exception ist ein Objekt → höhere Allokationsrate → häufigere Young-Gen-GCs, besonders unter Last mit vielen parallelen Requests.
- **Deopt-Churn als Latenz-Jitter:** Wird derselbe Code-Pfad (z. B. eine Service-Methode mit gelegentlicher `catch`-Nutzung) wiederholt de- und rekompiliert, äußert sich das als sporadische Latenzspitzen einzelner Requests, nicht als konstanter Overhead – schwer zu diagnostizieren ohne `-XX:+PrintCompilation`/`-XX:+TraceDeoptimization` oder JFR (`jdk.Deoptimization`-Events).
- **Thread-Modell:** Ob klassischer Tomcat-Threadpool oder virtuelle Threads (seit Tomcat 10.1/Servlet 6 mit `VirtualThreadExecutor`, Java 21+) – die Exception-Mechanik selbst ändert sich nicht, Unwinding läuft pro (virtuellem) Thread identisch. Uncaught Exceptions werden von Tomcats `StandardWrapperValve`/`ErrorReportValve` abgefangen, der Thread kehrt danach normal in den Pool zurück bzw. der virtuelle Thread wird beendet.
- **Praktische Konsequenz:** Exceptions für Fehlerfälle (selten, wirklich außergewöhnlich) sind praktisch kostenlos relevant. Exceptions als regulärer Kontrollfluss (z. B. für Validierung bei jedem Request) sind der Fall, in dem Stacktrace-Kosten, GC-Druck und Deopt-Churn tatsächlich ins Gewicht fallen – dort lohnt sich oft `fillInStackTrace()` in eigenen Exception-Klassen zu überschreiben (`return this;`) oder ganz auf Exceptions als Signalmechanismus zu verzichten.

Here are the sources and the English translation.

## Sources

- Stack trace cost & preallocated exceptions: benchmarks show creating a `new Exception()` costs roughly 1200ns versus ~8ns for a simple object like `Date`, and overriding `fillInStackTrace()` to return `this` eliminates that cost
- `OmitStackTraceInFastThrow` mechanics: this JDK 5 C2 compiler optimization stops emitting stack traces for exceptions that have already been thrown a few times, and can be disabled with `-XX:-OmitStackTraceInFastThrow`. Oracle's original release note describes it as: after an exception is thrown a few times, the method may be recompiled, after which the compiler may switch to preallocated exceptions without a stack trace
- Which exceptions this applies to: implicit exceptions — NullPointerException, ArrayIndexOutOfBoundsException, ArithmeticException, ArrayStoreException, ClassCastException
- Effect on NPE helpful messages: if the stack trace is omitted for an NPE, the backtrace data structure is missing and no detailed message is returned
- Deoptimization / uncommon traps: deoptimizations requested by the executing thread itself (e.g. from a failed null check) are called synchronous events, also known as uncommon traps in HotSpot; asynchronous events are triggered by another thread, such as during class hierarchy analysis; methods can only be deoptimized at safepoints
- What happens to the compiled code on deopt: deoptimization means the compiler's optimistic assumption was proven wrong, so execution switches from compiled code back to the interpreter by reconstructing interpreter state, and the compiled code may either be discarded or recompiled
- Uncommon trap use for uncompiled/exception paths: C2 inserts an uncommon trap in generated code, for example when a class that was uninitialized at compile time needs runtime initialization
- Repeated trap → permanently uncompilable: when the overflow recompile count exceeds `PerBytecodeRecompilationCutoff`, HotSpot marks the method as not-compilable at full optimization
- Cold exception handler path deopt reason: HotSpot has a dedicated deoptimization reason, `Reason_not_compiled_exception_handler`, for when an exception handler that wasn't compiled needs to be reached

Note: the `fillInStackTrace()` self-inspection detail, `athrow`/exception-table unwinding mechanics, `monitorexit` on frame pop, and Tomcat's `ErrorReportValve`/thread-pool behavior are standard JVM/Servlet-spec architecture that I described from established technical knowledge rather than a single citable page — happy to dig up JLS/JVMS section references specifically if useful.

---

## Translation

**1. Object creation + stack trace capture**

When you `throw new FooException()`, normal object allocation happens, then the `Throwable` constructor calls the native `fillInStackTrace()`. This runs synchronously in the throwing thread itself (no safepoint needed, since it's inspecting its own stack) and builds a compact internal backtrace structure — **not** yet the finished `StackTraceElement[]` array. That array is only materialized lazily when `getStackTrace()`/`printStackTrace()` is called.

For JVM-internal exceptions (NPE, `ArithmeticException`, `ArrayIndexOutOfBoundsException`, `ClassCastException`), there's a specific optimization: `-XX:+OmitStackTraceInFastThrow` (on by default). If the same implicit exception is thrown repeatedly (hot) at the same bytecode location, HotSpot replaces the allocation + stack-trace-fill step with a preallocated, stack-trace-less instance ("...<no stack trace available>"). This is the classic reason exceptions used as control flow in loops eventually show up with no stack trace at all — and it's also why NPE's helpful detailed messages disappear once this kicks in, since the backtrace data the message relies on is missing.

**2. `athrow` and stack unwinding**

The `athrow` bytecode instruction pops the reference and starts the handler search: the JVM (interpreter or compiled code, via the method's exception table/OopMaps) checks whether the current frame has a matching handler (PC range + type check via the class hierarchy). No match → the frame is popped, during which:
- `monitorexit` runs for every active `synchronized` block/method in that frame (releasing locks)
- local variables become eligible for GC
- the search continues recursively in the caller frame

`finally` hasn't been implemented via `jsr/ret` since Java 6; instead the compiler duplicates the `finally` bytecode inline at every exit path, plus a catch-all handler entry in the exception table.

If no handler exists anywhere on the stack → `Thread.dispatchUncaughtException` → `UncaughtExceptionHandler`.

## JIT / deoptimization

This is where the real cost lies if things go wrong:

- **C2 profiles exception paths.** If a `catch` branch is rarely or never taken during profiling, C2 often doesn't compile it in fully — it places an **uncommon trap** there instead. If that path is then actually hit at runtime, it triggers a **deoptimization of that specific method**: execution falls back to the interpreter for that frame, and continues interpreted from there.
- Whether the method gets recompiled afterward depends on the trap history (`PerMethodTrapLimit`/`PerBytecodeTrapLimit`). If the trap fires too often, HotSpot permanently marks the method as no longer compilable for that path at full optimization — it stays interpreted or degraded.
- Simply throwing/catching exceptions through code that was already compiled with the handler correctly baked in from the start does **not** cause deoptimization — unwinding through compiled frames runs through generated runtime stubs, GC-safe via OopMaps, without falling back to the interpreter.

In short: deopt doesn't happen because of the throw itself, but when the exception activates a path the compiler had classified as "cold" at compile time.

## Impact on a Tomcat web application

- **Stack trace cost scales with stack depth.** With Spring/Hibernate stacks of 60–150+ frames per request, `fillInStackTrace()` is a measurable CPU cost under a high exception rate (e.g., validation exceptions used as control flow).
- **Allocation pressure:** every exception is an object → higher allocation rate → more frequent young-gen GCs, especially under load with many concurrent requests.
- **Deopt churn as latency jitter:** if the same code path (e.g., a service method with occasional `catch` usage) gets repeatedly de-/recompiled, this shows up as sporadic latency spikes on individual requests rather than constant overhead — hard to diagnose without `-XX:+PrintCompilation`/`-XX:+TraceDeoptimization` or JFR (`jdk.Deoptimization` events).
- **Threading model:** whether classic Tomcat thread pool or virtual threads (since Tomcat 10.1/Servlet 6 with `VirtualThreadExecutor`, Java 21+), the exception mechanism itself doesn't change — unwinding works identically per (virtual) thread. Uncaught exceptions are caught by Tomcat's `StandardWrapperValve`/`ErrorReportValve`; afterward the thread returns normally to the pool, or the virtual thread terminates.
- **Practical takeaway:** exceptions for genuine error cases (rare, truly exceptional) are essentially free in practice. Exceptions used as regular control flow (e.g., validation on every request) are the case where stack trace cost, GC pressure, and deopt churn actually matter — there it's often worth overriding `fillInStackTrace()` in your own exception classes (`return this;`) or avoiding exceptions as a signaling mechanism altogether.