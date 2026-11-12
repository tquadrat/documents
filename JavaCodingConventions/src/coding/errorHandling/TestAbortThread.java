public final class TestAbortThread
{
    public static final void main( final String... args )
    {
        try
        {
            final var thread = new Thread( () -> 
            {
                throw new Error( "Thread aborted!" );
            } );
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
//  class TestAbortThread
