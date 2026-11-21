import static java.lang.System.lineSeparator;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;

import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 *  <p>{@summary A wrapper exception that can be used as base class for a
 *  domain specific root exception.}</p>
 *  <p>The derived exception classes can be used exclusively as a wrapper 
 *  for other exceptions. It does not populate the stack trace.</p>
 *  <p>When wrapping other instances of {@code WrapperException}, their
 *  {@code cause} is stored as the cause of the new instance.</p>
 */
public abstract class WrapperException extends RuntimeException
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

        /*--------------*\
    ====** Constructors **================================================
        \*--------------*/
    /**
     *  <p>{@summary Constructs a new instance of this
     *  {@code WrapperException} with the specified detail message and
     *  cause.}</p>
     *
     *  @param  message The detail message that is saved for later
     *      retrieval by the
     *      {@link #getMessage()}
     *      method.
     *  @param  cause   The cause that is saved for later retrieval by the
     *     {@link #getCause()}
     *     method.
     */
    protected WrapperException( final String message, final Throwable cause )
    {
        final Throwable effectiveCause;
        final String effectiveMessage;
        if( requireNonNullArgument( cause, "cause" ) instanceof WrapperException wrapper )
        {
            effectiveCause = wrapper.getCause();
            effectiveMessage = mapFromNull( message, cause::getMessage );
        }
        else
        {
            effectiveCause = cause;
            effectiveMessage = mapFromNull( message, cause::toString );
        }
        super( effectiveMessage, effectiveCause );

        if( cause instanceof WrapperException wrapper )
        {
            if( !Objects.equals( message, wrapper.getInternalMessage() ) )
            {
                addInfo( "Additional Message", "%1$s: %2$s".formatted( wrapper.getClass().getName(), wrapper.getInternalMessage() ) );
            }
            for( final var entry : wrapper.getAdditionalInfo().entrySet() )
            {
                final var data = m_AdditionalInfo.computeIfAbsent( entry.getKey(), _ -> new ArrayList<>() );
                data.addAll( 0, entry.getValue() );
            }
        }
    }   //  WrapperException()

    /**
     *  <p>{@summary Constructs a new instance of this
     *  {@code WrapperException} that wraps the specified cause.}</p>
     *
     *  @param  cause   The cause that is saved for later retrieval by the
     *     {@link #getCause()}
     *     method.
     */
    protected WrapperException( final Throwable cause )
    {
        requireNonNullArgument( cause, "cause" );
        final var message = cause instanceof WrapperException wrapper
            ? wrapper.getInternalMessage()
            : cause.toString();
        this( message, cause );
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
        data.addFirst( ( isNull( message) || message.isBlank() ) ? "<No Message>" : message );
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
        final var buffer = new StringJoiner( lineSeparator(), super.getMessage().concat( lineSeparator() ), EMPTY_STRING );
        buffer.setEmptyValue( super.getMessage() );
        for( final var info : m_AdditionalInfo.entrySet() )
        {
            buffer.add(
                info.getValue()
                    .stream()
                    .collect( joining( "], [", "%s: [".formatted( info.getKey() ), "]" ) )
            );
        }

        final var retValue = buffer.toString();

        //---* Done *-----------------------------------------------------
        return retValue;
    }   //  getMessage()
}
//  class WrapperException
