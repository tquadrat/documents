/**
 *  <p>{@summary This is a specialised implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where {@null} is
 *  provided as an illegal argument value.}</p>
 */
public final class NullArgumentException extends IllegalArgumentException
{
        /*-----------*\
    ====** Constants **===================================================
        \*-----------*/
    /**
     *  <p>{@summary The replacement text for an unknown parameter name:
     *  {@value}.}</p>
     */
    public static final String UNKNOWN_PARAM_NAME = "<unknown>";

        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary {@constructor}}</p>
     */
    public NullArgumentException() { super( "Argument must not be null" ); }

    /**
     *  <p>{@summary {@constructor}}</p>
     *
     *  @param argName The name of the parameter whose value was provided
     *      as {@null}; if {@null} or the empty String, a default is used
     *      as the name of the parameter.
     */
    public NullArgumentException( final String argName )
    {
        super( """
````````````Argument "%1$s" must not be null\
````````````""".formatted( mapFromEmpty( argName, UNKNOWN_PARAM_NAME ) ) );
    }   //  NullArgumentException()
}
//  class NullArgumentException
