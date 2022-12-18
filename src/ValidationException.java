package org.tquadrat.foundation.exception;

import static org.tquadrat.foundation.lang.Objects.isNull;

import java.io.Serial;

/**
 *  This is a specialized implementation for the
 *  {@link IllegalArgumentException}
 *  that is meant as the root for a hierarchy of exceptions caused by
 *  validation errors.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public class ValidationException extends IllegalArgumentException
{
        /*-----------*\
    ====** Constants **==============================================
        \*-----------*/
    /**
     *  The message text for a validation error.
     */
    public static final String MSG_ValidationFailed = "Validation failed";

        /*------------------------*\
    ====** Static Initialisations **=================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     * @hidden
     */
    @Serial
    private static final long serialVersionUID = 1174360235354917591L;

        /*--------------*\
    ====** Constructors **===========================================
        \*--------------*/
    /**
     *  Creates a new {@code ValidationException} instance.
     */
    public ValidationException()
    {
        super( MSG_ValidationFailed );
    }   //  ValidationException()

    /**
     *  Creates a new {@code ValidationException} instance.
     *
     *  @param  message The message that provides details on the
     *      failed validation.
     */
    public ValidationException( final String message )
    {
        super( isNull( message ) || message.isEmpty() ? MSG_ValidationFailed : message );
    }   //  ValidationException()

    /**
     *  Creates a new {@code ValidationException} instance.
     *
     *  @param  message The message that provides details on the failed
     *      validation.
     *  @param  cause   The exception that is related to the validation
     *      error.
     */
    public ValidationException( final String message, final Throwable cause )
    {
        super( isNull( message ) || message.isEmpty() ? MSG_ValidationFailed : message, cause );
    }   //  ValidationException()

    /**
     *  Creates a new {@code ValidationException} instance.
     *
     *  @param  cause   The exception that is related to the validation
     *      error.
     */
    public ValidationException( final Throwable cause )
    {
        super( MSG_ValidationFailed, cause );
    }   //  ValidationException()
}
//  class ValidationException

