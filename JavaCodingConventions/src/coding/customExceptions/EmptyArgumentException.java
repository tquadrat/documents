/**
 *  <p>{@summary This is a specialised implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where an empty
 *  String, an empty array or an empty
 *  {@link Collection}
 *  argument is provided as an illegal argument value.}</p>
 */
public final class EmptyArgumentException extends IllegalArgumentException
{
        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary {@constructor}}</p>
     */
    public EmptyArgumentException() { super( "Argument must not be empty" ); }

    /**
     *  <p>{@summary {@constructor}}</p>
     *
     *  @param  argName The name of the parameter whose value was provided
     *      as empty; if {@null} or the empty String, a default is used as
     *      the name of the parameter.
     */
    public EmptyArgumentException( final String argName )
    {
        super( """
````````````Argument "%1$s" must not be empty
````````````""".formatted( mapFromEmpty( argName, UNKNOWN_PARAM_NAME ) ) );
    }   //  EmptyArgumentException()
}
//  class EmptyArgumentException
