package org.tquadrat.util.concurrent;

import static java.util.Objects.requireNonNull;
import java.util.concurrent.locks.Lock;

/**
 *  A wrapper for locks that supports the {@code try-with-resources}
 *  feature of Java 7.
 *  The creation of the local reference to the wrapper object means
 *  some overhead but in very most scenarios this is negligible.
 *
 *  {@code AutoLock} will only expose the methods
 *  {@link #lock()}
 *  and
 *  {@link #lockInterruptibly()}
 *  of the interface
 *  {@link java.util.concurrent.locks.Lock Lock},
 *  but with a return value. Exposing other methods is not
 *  reasonable.
 *  Calling
 *  {@link #close()}
 *  on the {@code AutoLock} instance or
 *  {@link Lock#unlock()}
 *  on the wrapped {@code Lock} object inside the {@code try} block
 *  may cause unpredictable effects.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *
 *  @see java.util.concurrent.locks.Lock
 */
public class AutoLock implements AutoCloseable
{
        /*------------*\
    ====** Attributes **=============================================
        \*------------*/
    /**
     *  The wrapped lock.
     */
    private final Lock m_Lock;
    
        /*--------------*\
    ====** Constructors **===========================================
        \*--------------*/
    /**
     *  Creates a new {@code AutoLock} object.
     *
     *  @param  lock    The wrapped lock.
     */
    public AutoLock( final Lock lock )
    {
        m_Lock = requireNonNull( lock );
    }   //  AutoLock()
    
        /*---------*\
    ====** Methods **================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void close() { m_Lock.unlock(); }

    /**
     *  Calls
     *  {@link java.util.concurrent.locks.Lock#lock() lock()}
     *  on the wrapped
     *  {@link java.util.concurrent.locks.Lock}
     *  instance.
     *
     *  @return The reference to this {@code AutoLock} instance.
     */
    public final AutoLock lock()
    {
        m_Lock.lock();

        //---* Done *------------------------------------------------
        return this;
    }   //  lock()
}
//  class AutoLock

