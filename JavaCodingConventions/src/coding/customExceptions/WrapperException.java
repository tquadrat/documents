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
 *  <p>The derived exception classes must provide the usual constructors
 *  that set the detailed message and the cause; these will call
 *  {@link #WrapperException(String,Throwable,boolean)}.</p>
 *  <p>A derived class can be used exclusively as a wrapper or container
 *  for other exceptions when it will create its instances with
 *  {@code retrieveStack} set to {@code false}, meaning that it does not
 *  populate the stack trace. Such a container requires that {@code cause}
 *  is not {@code null} and when wrapping other instances of 
 *  {@code WrapperException}, their {@code cause} is stored as the cause
 *  of the new instance.</p>
 *  <p>If a derived class should be extended by other exception classes
 *  with different behaviour, it is mandatory to expose the constructor
 *  {@link #WrapperException(String,Throwable,boolean)},
 *  too.</p>
 *
 *  @note Each hierarchy of domain specific exceptions should have only
 *      one wrapper or container exception (an exception class that sets
 *      {@code retrieveStack} to {@code false}).
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
     *  <p>{@summary Constructs a new instance of {@class} with the
     *  specified detail message, cause, and whether the stack trace
     *  should be retrieved.}</p>
     *  <p>If {@code retrieveStacktrace} is {@code false}, the instance is
     *  a pure wrapper or container for other exceptions; consequently
     *  {@code cause} cannot be {@code null} if {@code retrieveStacktrace}
     *  is {@code false}. In that case, if {@code cause} is an instance of
     *  {@code WrapperException}, it will be replaced by
     *  {@linkplain Throwable#getCause() its cause}
     *  if that is not {@code null}. In that case, the additional
     *  information is merged, too.</p>
     *  <p>If {@code message} is {@code null} and {@code cause} is an
     *  instance of {@code WrappedException}, the internal message of
     *  {@code cause} is used instead.</p>
     *  <p>If {@code message} is {@code null} and {@code cause} is another
     *  exception type, the result of
     *  {@link Throwable#toString() cause::toString}
     *  is used as the detailed message for the new instance.</p>
     *
     *  @param  message The detail message that is saved for later
     *      retrieval by the
     *      {@link #getMessage()}
     *      method. It can be {@code null}.
     *  @param  cause   The cause that is saved for later retrieval by the
     *     {@link #getCause()}
     *     method. It can be {@code null\.
     *  @param retrieveStacktrace   {@code true} if the stack trace will
     *      be retrieved and is writable, {@code false} if the instance
     *      should behave like a <i>regular</i> exception.
     */
    protected WrapperException( final String message, final Throwable cause, final boolean retrieveStacktrace )
    {
        final String effectiveMessage;
        final Throwable effectiveCause;
        boolean mergeAdditionalInfo = false;
        if( retrieveStacktrace )
        {
            //---* Basically the normal exception behaviour *-------------
            effectiveCause = cause;
            effectiveMessage = nonNull( cause )
                ? mapFromNull( message, cause instanceof WrapperException wrapper
                    ? wrapper.getInternalMessage()
                    : cause.toString() )
                : message;
        }
        else
        {
            //---* The instance just wraps other exceptions *-------------
            if( requireNonNullArgument( cause, "cause" ) instanceof WrapperException wrapper )
            {
                mergeAdditionalInfo = true;
                effectiveCause = wrapper.getCause();
                effectiveMessage = mapFromNull( message, wrapper.getInternalMessage() );
            }
            else
            {
                effectiveCause = cause;
                effectiveMessage = mapFromNull( message, cause::toString );
            }
        }

        //---* Create the instance *--------------------------------------
        super( effectiveMessage, effectiveCause, true, retrieveStacktrace );

        //---* Replicate the additional info *----------------------------
        if( mergeAdditionalInfo )
        {
            final var wrapper = (WrapperException) cause;
            if( !Objects.equals( effectiveMessage, wrapper.getInternalMessage() ) )
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
        final var message = super.getMessage();
        final StringJoiner buffer;
        if( nonNull( message ) )
        {
            buffer = new StringJoiner( lineSeparator(), message.concat( lineSeparator() ), EMPTY_STRING );
            buffer.setEmptyValue( message );
        }
        else
        {
            buffer = new StringJoiner( lineSeparator() );
        }
        for( final var info : m_AdditionalInfo.entrySet() )
        {
            buffer.add(
                info.getValue()
                    .stream()
                    .collect( joining( "], [", "%s: [".formatted( info.getKey() ), "]" ) )
            );
        }

        final var retValue = m_AdditionalInfo.isEmpty() ? message : buffer.toString();

        //---* Done *-----------------------------------------------------
        return retValue;
    }   //  getMessage()
}
//  class WrapperException