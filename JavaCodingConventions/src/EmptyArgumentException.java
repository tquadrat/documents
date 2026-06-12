package org.tquadrat.foundation.exception;

import java.io.Serial;

/**
 *  This is a specialized implementation for the
 *  {@link IllegalArgumentException}
 *  that should be used instead of the latter in cases where an empty
 *  String, array or
 *  {@link java.util.Collection}
 *  argument is provided as an illegal argument value.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public final class EmptyArgumentException extends NullArgumentException
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
     *  Creates a new instance of {@code EmptyArgumentException}.
     */
    public EmptyArgumentException() { this( null ); }

    /**
     *  Creates a new instance of {@code EmptyArgumentException}.
     *
     *  @param  argName The name of the argument whose value was
     *      provided as empty; if {@code null} or the empty String, 
     *      a default message is used that does not use the name of 
     *      the argument.
     */
    public EmptyArgumentException( final String argName )
    {
        super( argName, "Argument '%1$s' must not be empty", "Argument must not be empty" );
    }   //  EmptyArgumentException()
}
//  class EmptyArgumentException

