package jcc.printing.exception;

import static java.lang.System.lineSeparator;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;

import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  <p>{@summary A wrapper exception that can be used as base class for a
 *  domain specific root exception.}</p>
 *  <p>The exception class is to be used exclusively as a wrapper for other
 *  exceptions. It does not populate the stack trace.</p>
 */
public class WrapperException extends RuntimeException
{
        /*------------*\
    ====** Attributes **==================================================
        \*------------*/
    /**
     *  <p>{@summary The container for the additional information.}</p>
     */
    private final Map<String,List<String>> m_AdditionalInfo = new LinkedHashMap<>();

        /*------------------------*\
    ====** Static Initialisations **======================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 11743602353543445L;
(|\clearpage|)
        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary Constructs a new instance of this 
     *  {@code WrapperException} with the specified detail message and
     *  cause.}</p>
     *  <p>Note that the detail message associated with {@code cause} is
     *  <i>not</i> automatically incorporated in this exception's detail
     *  message.
     *
     *  @param  message The detail message that is saved for later
     *      retrieval by the
     *      {@link #getMessage()}
     *      method.
     *  @param  cause   The cause that is saved for later retrieval by the
     *     {@link #getCause()}
     *     method.
     */
    public WrapperException( final String message, final Throwable cause )
    {
        super( requireNotBlankArgument( message, "message" ), requireNonNullArgument( cause, "cause" ) );
    }   //  WrapperException()

    /**
     *  <p>{@summary Constructs a new instance of this 
     *  {@code WrapperException} that wraps the specified cause.}</p>
     *
     *  @param  cause   The cause that is saved for later retrieval by the
     *     {@link #getCause()}
     *     method.
     */
    public WrapperException( final Throwable cause )
    {
        super( requireNonNullArgument( cause, "cause" ) );
    }   //  WrapperException()

    /**
     *  <p>{@summary Constructs a new instance of this
     *  {@code WrapperException} with the specified detail message that
     *  wraps the other instance of {@code WrapperException}.}</p>
     *
     *  @param  <E> The type of the instance to wrap.
     *  @param  message The detail message that is saved for later
     *      retrieval by the
     *      {@link #getMessage()}
     *      method.
     *  @param  other   The other instance to wrap..
     */
    public <E extends WrapperException> WrapperException( final String message, final E other )
    {
        super( requireNotBlankArgument( message, "message" ), requireNonNullArgument( other, "other" ).getCause() );
        for( final var entry : other.getAdditionalInfo().entrySet() )
        {
            final var data = m_AdditionalInfo.computeIfAbsent( entry.getKey(), _ -> new ArrayList<>() );
            data.addAll( 0, entry.getValue() );
        }
        if( !message.equals( other.getInternalMessage() ) )
        {
            addInfo( "additional Message", other.getInternalMessage() );
        }
    }   //  WrapperException()

    /**
     *  <p>{@summary Constructs a new instance of this
     *  {@code WrapperException}
     *  that wraps the specified other instance of
     *  {@code WrapperException}.}</p>
     *
     *  @param  <E> The type of the instance to wrap.
     *  @param  other   The other instance to wrap..
     */
    public <E extends WrapperException> WrapperException( final E other )
    {
        this( requireNonNullArgument( other, "other" ).getInternalMessage(), other );
    }   //  WrapperException()

        /*---------*\
    ====** Methods **=====================================================
        \*---------*/
    /**
     *  <p>{@summary Adds information to this exception instance.}</p>
     *
     *  @param  key The information type.
     *  @param  message The information data.
     */
    public final void addInfo( final String key, final String message )
    {
        final var data = m_AdditionalInfo.computeIfAbsent( mapFromNull( key, "[null]"), _ -> new ArrayList<>() );
        data.addFirst( isEmptyOrBlank( message ) ? "<No Message>" : message );
    }   //  addInfo()

    /**
     *  <p>{@summary This method does nothing.} Consequently, the
     *  stacktrace for this exception instance does not get populated.</p>
     *
     *  @return A reference to this {@code Throwable} instance.
     *
     *  @see Throwable#printStackTrace()
     */
    @Override
    public final synchronized Throwable fillInStackTrace() { return this; }

    /**
     *  <p>{@summary Return the additional infos.}</p>
     *
     *  @return The additional info.
     */
    protected final Map<String,List<String>> getAdditionalInfo()
    {
        final var retValue = unmodifiableMap( m_AdditionalInfo );

        //---* Done *-----------------------------------------------------
        return retValue;
    }   //  getAdditionalInfo()

    /**
     *  <p>{@summary Returns the internal message.}</p>
     *
     *  @return The internal message.
     */
    protected final String getInternalMessage() { return super.getMessage(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getMessage()
    {
        final var buffer = new StringBuilder( super.getMessage() )
            .append( lineSeparator() );
        for( final var info : m_AdditionalInfo.entrySet() )
        {
            buffer.append(
                info.getValue()
                    .stream()
                    .collect( joining( "], [", "%s: [".formatted( info.getKey() ), "]".concat( lineSeparator() ) ) )
            );
        }

        final var retValue = buffer.toString();

        //---* Done *-----------------------------------------------------
        return retValue;
    }   //  getMessage()
}
//  class WrapperException
