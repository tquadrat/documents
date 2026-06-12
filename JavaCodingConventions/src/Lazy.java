package org.tquadrat.foundation.lang;

import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.internal.LazyImpl;

/**
 *  <p>{@summary A holder for a lazy initialised object 
 *  instance.}</p>
 *  <p>An instance of an implementation of this interface holds a
 *  value that will be initialised by a call to the supplier 
 *  (provided with the method
 *  {@link #use(Supplier)})
 *  on a first call to
 *  {@link #get()}.</p>
 *  <p>Use
 *  {@link #isPresent()}
 *  to avoid unnecessary initialisation.</p>
 *  <p>As a lazy initialisation makes the value unpredictable, it is 
 *  necessary that the implementations of
 *  {@link #equals(Object)}
 *  and
 *  {@link #hashCode()}
 *  do force the initialisation.</p>
 *  <p>{@link #toString()}
 *  will not force the initialisation.</p>
 *  <p>This interface is not feasible for the lazy initialisation of
 *  connections to resources of any kind, as these will usually 
 *  throw (checked) exceptions when problems arise, and usually these
 *  should be handled on one central location.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *
 *  @param  <T> The type of the value for this instance of 
 *  {@code Lazy}.
 */
public sealed interface Lazy<T>
    permits org.tquadrat.foundation.lang.internal.LazyImpl
{
        /*---------*\
    ====** Methods **================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean equals( final Object obj );

    /**
     *  Returns the value for this instance of {@code Lazy}.
     *
     *  @return The value.
     */
    public T get();

    /**
     *  {@inheritDoc}
     */
    @Override
    public int hashCode();

    /**
     *  If this {@code Lazy} instance has been initialised already, 
     *  the provided
     *  {@link Consumer}
     *  will be executed; otherwise nothing happens.
     *
     *  @param  consumer    The consumer.
     */
    public default void ifPresent( final Consumer<? super T> consumer )
    {
        if( isPresent() ) requireNonNullArgument( consumer, "consumer" ).accept( get() );
    }   //  ifPresent()

    /**
     *  Checks whether this {@code Lazy} instance has been 
     *  initialised already. But even it was initialised,
     *  {@link #get()}
     *  may still return {@code null}.
     *
     *  @return {@code true} if the instance was initialised, 
     *      {@code false} otherwise.
     */
    public boolean isPresent();

    /**
     *  If this instance of {@code Lazy} is initialised, the provided 
     *  mapper function will be executed on the value.
     *
     *  @param  <R> The result type for the mapper function.
     *  @param  mapper  The mapper function.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the result for the mapping.
     */
    public default <R> Optional<R> map( final Function<? super T,? extends R> mapper )
    {
        final Optional<R> retValue = isPresent() ? Optional.ofNullable( mapper.apply( get() ) ) : Optional.empty();

        //---* Done *------------------------------------------------
        return retValue;
    }   //  map()

    /**
     *  Returns the value or throws the exception that is created by 
     *  the given
     *  {@link Supplier}
     *  when not yet initialised.
     *
     *  @param  <X> The type of the implementation of
     *      {@link Throwable}.
     *  @param  exceptionSupplier   The supplier for the exception 
     *      to throw when the instance was not yet initialised.
     *  @return The value.
     *  @throws X   When not initialised, the exception created by 
     *      the given supplier will be thrown.
     */
    public <X extends Throwable> T orElseThrow( Supplier<? extends X> exceptionSupplier ) throws X;

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString();

    /**
     *  Creates a new {@code Lazy} instance that uses the given 
     *  supplier to initialise.
     *
     *  @param  <T> The type of the value for the new instance of 
     *      {@code Lazy}.
     *  @param  supplier    The supplier that initialises the value 
     *      for this instance on the first call to
     *      {@link #get()}.
     *  @return The new instance.
     */
    public static <T> Lazy<T> use( final Supplier<T> supplier )
    {
        return new LazyImpl<>( supplier );
    }   //  use()
}
//  interface Lazy

