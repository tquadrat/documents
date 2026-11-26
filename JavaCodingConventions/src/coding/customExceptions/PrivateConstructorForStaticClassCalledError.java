/**
 *  <p>{@summary This implementation of
 *  {@link Error}
 *  should be called from {@code private} constructors of static classes
 *  (classes that does not allow any instances because they have only static
 *  methods or constants).} The message that is stored with the error is the
 *  name of the respective class.</p>
 */
public class PrivateConstructorForStaticClassCalledError extends AssertionError
{
        /*------------------------*\
    ====** Static Initialisations **======================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary {@constructor}}</p>
     *
     *  @param  message Should be the name of the respective static class.
     */
    public PrivateConstructorForStaticClassCalledError( final String message ) { super( requireNonNullArgument( message, "message" ) ); }

    /**
     *  <p>{@summary {@constructor}}</p>
     *
     *  @param  theClass    The respective static class.
     */
    public PrivateConstructorForStaticClassCalledError( final Class<?> theClass ) { this( requireNonNullArgument( theClass, "theClass" ).getName() ); }
}
//  class PrivateConstructorForStaticClassCalledError
