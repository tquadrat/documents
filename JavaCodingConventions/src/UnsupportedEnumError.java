package org.tquadrat.foundation.exception;

import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.lang.internal.SharedFormatter.format;

import java.io.Serial;

/**
 *  This is a specialized implementation for
 *  {@link Error}
 *  that is to be thrown especially from the {@code default} branch
 *  of a {@code switch} statement that uses an {@code enum} type as
 *  selector.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public final class UnsupportedEnumError extends Error
{
        /*-----------*\
    ====** Constants **==============================================
        \*-----------*/
    /**
     *  The message text.
     */
    private static final String MSG_UnsupportedEnum = "The value '%2$s' of enum class '%1$s' is not supported";

        /*------------------------*\
    ====** Static Initialisations **=================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1174360235354917591L;

        /*--------------*\
    ====** Constructors **===========================================
        \*--------------*/
    /**
     *  Creates a new instance of this class.
     *
     *  @param  <T> The type of the enum.
     *  @param  value   The unsupported value.
     */
    public <T extends Enum<T>> UnsupportedEnumError( final T value )
    {
        super( format( MSG_UnsupportedEnum, requireNonNullArgument( value, "value" ).getClass().getName(), value.name() ) );
    }   //  UnsupportedEnumError()

    /**
     *  Creates a new instance of this class.
     *
     *  @param  type    The class of the enum.
     *  @param  value   The unsupported value.
     */
    public UnsupportedEnumError( final Class<? extends Enum<?>> type, final String value )
    {
        super( format( MSG_UnsupportedEnum, requireNonNullArgument( type, "type" ).getName(), requireNotEmptyArgument( value, "value" ) ) );
    }   //  UnsupportedEnumError()
}
//  class UnsupportedEnumError

