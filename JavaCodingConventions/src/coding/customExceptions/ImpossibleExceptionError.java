/**
 *  <p>{@summary This implementation of
 *  {@link Error}
 *  should be thrown in all cases where an exception was caught that seemed to
 *  be impossible to be thrown.}</p>
 *  <p>This is different from the
 *  {@link UnlikelyExceptionError}
 *  as there there exception is possible in general, but unlikely in the
 *  particular context.</p>
 */
public final class ImpossibleExceptionError extends UnlikelyExceptionError
{
        /*-----------*\
    ====** Constants **===================================================
        \*-----------*/
    /**
     *  The text for the default message.
     */
    public static final String MSG_ImpossibleException = "The Exception '%1$s' was not expected and should never occur in this context";

        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary {@constructor}} The message
     *  is a constant, only the causing exception can be given.</p>
     *
     *  @param  cause   The causing exception.
     */
    public ImpossibleExceptionError( final Throwable cause )
    {
        super( MSG_ImpossibleException.formatted( requireNonNullArgument( cause, "cause" ).getClass().getName() ) , cause );
    }   //  ImpossibleExceptionError()

    /**
     *  <p>{@summary {@constructor}} This
     *  constructor should be used in cases where an enhanced message is useful
     *  or necessary.</p>
     *
     *  @param  message The message for the error.
     *  @param  cause   The causing exception.
     */
    public ImpossibleExceptionError( final String message, final Throwable cause )
    {
        super( mapFromEmpty( message, MSG_ImpossibleException.formatted( requireNonNullArgument( cause, "cause" ).getClass().getName() ) ), requireNonNullArgument( cause, "cause" ) );
    }   //  ImpossibleExceptionError()
}
//  class ImpossibleExceptionError

/*
 *  End of File
 */
