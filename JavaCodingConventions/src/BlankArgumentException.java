package org.tquadrat.foundation.exception;

import java.io.Serial;

/**
 *  This is a specialized implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where a blank 
 *  String is provided as an illegal argument value.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public final class BlankArgumentException extends NullArgumentException
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
     *  Creates a new instance of {@code BlankArgumentException}.
     */
    public BlankArgumentException() { this( null ); }

    /**
     *  Creates a new instance of {@code BlankArgumentException}.
     *
     *  @param  argName The name of the argument whose value was
     *      provided as blank; if {@code null} or the empty String, 
     *      a default message is used that does not use the name of 
     *      the argument.
     */
    public BlankArgumentException( final String argName )
    {
        super( argName, "Argument '%1$s' must not be blank", "Argument must not be blank" );
    }   //  BlankArgumentException()
}
//  class BlankArgumentException

