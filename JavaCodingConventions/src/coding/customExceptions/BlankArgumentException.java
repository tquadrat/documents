/**
 *  <p>{@summary This is a specialised implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where a blank
 *  String is provided as an illegal argument value.}</p>
 */
public final class BlankArgumentException extends IllegalArgumentException
{
        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary {@constructor}}</p>
     */
    public BlankArgumentException() { super( "Argument must not be blank" ); }

    /**
     *  <p>{@summary {@constructor}}</p>
     *
     *  @param  argName The name of the parameter whose value was provided as
     *      blank; if {@null} or the empty String, a default is used as the
     *      name of the parameter.
     */
    public BlankArgumentException( final String argName )
    {
        super( """
````````````Argument "%1$s" must not be blank\
````````````""".formatted( mapFromEmpty( argName, UNKNOWN_PARAM_NAME ) ) );
    }   //  BlankArgumentException()
}
//  class BlankArgumentException
