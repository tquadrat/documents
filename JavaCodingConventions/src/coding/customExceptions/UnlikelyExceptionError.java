/**
 *  <p>{@summary This implementation of
 *  {@link Error}
 *  should be thrown in all cases where an <i>unlikely</i> exception was
 *  caught.}
 *  Different from an
 *  {@link ImpossibleExceptionError},
 *  the exception is <i>possible</i>, but <i>unlikely</i> – probably because
 *  the programmer has taken (at least thought to have &hellip;) appropriate
 *  precautions against its occurrence.
 */
public sealed class UnlikelyExceptionError extends AssertionError
    permits ImpossibleExceptionError
{
        /*-----------*\
    ====** Constants **===================================================
        \*-----------*/
    /**
     *  The message for this exception.
     */
    private static final String MSG_UnexpectedException = "The Exception '%1$s' was not expected to occur in this context";

        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary {@constructor}} The message is a constant, only
     *  the causing exception can be given.</p>
     *
     *  @param  cause   The causing exception.
     */
    public UnlikelyExceptionError( final Throwable cause )
    {
        super( MSG_UnexpectedException.formatted( requireNonNullArgument( cause, "cause" ).getClass().getName() ), cause );
    }   //  UnexpectedExceptionError()

    /**
     *  <p>{@summary {@constructor}} This constructor should be used in
     *  cases where an enhanced message is useful or necessary.</p>
     *
     *  @param  message The message for the error.
     *  @param  cause   The causing exception.
     */
    public UnlikelyExceptionError( final String message, final Throwable cause )
    {
        super( mapFromEmpty( message, MSG_UnexpectedException.formatted( requireNonNullArgument( cause, "cause" ).getClass().getName() ) ), requireNonNullArgument( cause, "cause" ) );
    }   //  UnexpectedExceptionError()
}
//  class UnexpectedExceptionError
