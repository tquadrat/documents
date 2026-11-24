package org.tquadrat.foundation.exception;

import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.nonNull;

import java.io.Serial;

/**
 *  This is a specialized implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where {@code null} is
 *  provided as an illegal argument value.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public sealed class NullArgumentException extends ValidationException
    permits BlankArgumentException, EmptyArgumentException
{
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
     *  Creates a new instance of {@code NullArgumentException}.
     */
    public NullArgumentException() { this( null ); }

    /**
     *  Creates a new instance of {@code NullArgumentException}.
     *
     *  @param  argName The name of the argument whose value was 
     *      provided as {@code null}; if {@code null} or the empty 
     *      String, a default message is used that does not use the
     *      name of the argument.
     */
    public NullArgumentException( final String argName )
    {
        this( argName, "Argument '%1$s' must not be null", "Argument must not be null" );
    }   //  NullArgumentException()

    /**
     *  <p>{@summary Creates a new instance of 
     *  {@code NullArgumentException}.}</p>
     *  <p>This constructor was introduced for the
     *  {@link EmptyArgumentException}.</p>
     *
     *  @param  argName The name of the argument whose value was 
     *      provided as {@code null}; if {@code null} or the empty
     *      String, a default message is used that does not use the
     *      name of the argument.
     *  @param  msgName The regular message.
     *  @param  msgNone The default message.
     */
    protected NullArgumentException( final String argName, final String msgName, final String msgNone )
    {
        super( nonNull( argName ) && !argName.isEmpty() ? msgName.formatted( argName ) ) : msgNone );
    }   //  NullArgumentException()
}
//  class NullArgumentException

