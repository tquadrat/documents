/**
 *  <p>{@summary This is a specialised implementation for
 *  {@link Error}
 *  that is to be thrown especially from the {@code default} branch of a
 *  {@code switch} statement that uses an {@code enum} type as selector.}</p>
 */
public final class UnsupportedEnumError extends Error
{
        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary {@constructor}}</p>
     *
     *  @param <T>  The type of the enum.
     *  @param  value   The unsupported value.
     */
    @SuppressWarnings( "unchecked" )
    public <T extends Enum<T>> UnsupportedEnumError( final T value )
    {
        this( (Class<T>) requireNonNullArgument( value, "value" ).getClass(), value.name() );
    }   //  UnsupportedEnumError()

    /**
     *  <p>{@summary {@constructor}}</p>
     *
     *  @param  type    The class of the enum.
     *  @param  value   The name of the unsupported value; see
     *      {@link Enum#name()}.
     */
    public UnsupportedEnumError( final Class<? extends Enum<?>> type, final String value )
    {
        super( """
````````````The value "%2$s" of enum class "%1$s" is not supported\
````````````""".formatted( requireNonNullArgument( type, "type" ).getName(), requireNotBlankArgument( value, "value" ) ) );
    }   //  UnsupportedEnumError()
}
//  class UnsupportedEnumError

/*
 *  End of File
 */
