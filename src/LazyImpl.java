package org.tquadrat.foundation.lang.internal;

import static org.tquadrat.foundation.lang.CommonConstants.NULL_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Optional;
import java.util.function.Supplier;

import org.tquadrat.foundation.lang.AutoLock;
import org.tquadrat.foundation.lang.Lazy;
import org.tquadrat.foundation.lang.Objects;

/**
 *  <p>{@summary The implementation of the interface
 *  {@link Lazy}.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *
 *  @param  <T> The type of the value for this instance of {@code Lazy}.
 */
public final class LazyImpl<T> implements Lazy<T>
{
        /*------------*\
    ====** Attributes **=============================================
        \*------------*/
    /**
     *  The lock that protects the value creation. It will be set to
     *  {@linkplain Optional#empty() empty}
     *  after
     *  {@link #m_Value} is initialised.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private Optional<AutoLock> m_Lock;

    /**
     *  The supplier for the value of this {@code Lazy} instance. It 
     *  will be set to {@code null} after
     *  {@link #m_Value}
     *  is initialised.
     */
    private Supplier<T> m_Supplier;

    /**
     *  The value of this {@code Lazy} instance; it is {@code null} 
     *  if it was not yet initialised.
     */
    private T m_Value;

        /*--------------*\
    ====** Constructors **===========================================
        \*--------------*/
    /**
     *  Creates a new {@code Lazy} instance.
     *
     *  @param  supplier    The supplier that initialises the value 
     *      for this instance on the first call to
     *      {@link #get()}.
     */
    public LazyImpl( final Supplier<T> supplier )
    {
        m_Supplier = requireNonNullArgument( supplier, "supplier" );
        m_Lock = Optional.of( AutoLock.of() );
        m_Value = null;
    }   //  LazyImpl()

        /*---------*\
    ====** Methods **================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean equals( final Object obj )
    {
        var retValue = this == obj;
        if( !retValue && nonNull( obj ) )
        {
            if( obj instanceof Lazy other )
            {
                retValue = get().equals( other.get() );
            }
            else
            {
                retValue = get().equals( obj );
            }
        }

        //---* Done *------------------------------------------------
        return retValue;
    }   //  equals()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final T get()
    {
        m_Lock.ifPresent( l ->
        {
            try( @SuppressWarnings( "unused" ) final var lock = l.lock() )
            {
                /*
                 * When the lock is present, the supplier must have 
                 * been not null; this is enforced by the respective 
                 * constructor.
                 * So when the supplier is NOW null, another thread 
                 * has performed the initialisation while this one 
                 * was waiting for the lock.
                 */
                if( nonNull( m_Supplier ) )
                {
                    m_Value = m_Supplier.get();
                    m_Lock = Optional.empty();
                    m_Supplier = null;
                }
            }
        } );

        //---* Done *------------------------------------------------
        return m_Value;
    }   //  get()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return get().hashCode(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isPresent() { return isNull( m_Supplier ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public <X extends Throwable> T orElseThrow( final Supplier<? extends X> exceptionSupplier ) throws X
    {
        if( !isPresent() ) throw exceptionSupplier.get();

        //---* Done *------------------------------------------------
        return m_Value;
    }   //  orElseThrow()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var retValue = Objects.toString( m_Value, 
            isPresent() 
                ? NULL_STRING 
                : "[Not initialized]" );

        //---* Done *------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class LazyImpl

