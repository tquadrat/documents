public final class TestAbortRun
{
    public static final void run()
    {
        throw new Error( "run() aborted!" );
    }   //  run()
    
    public static final void main( final String... args )
    {
        try
        {
            final var thread =  new Thread( TestAbortRun::run );
            thread.start();
            thread.join();
        }
        catch( final InterruptedException e )
        {
            e.printStackTrace();
        }
        throw new Error( "main() aborted!" );
    }   //  main()
}
//  class TestAbortRun
