/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.collection;

import com.google.common.collect.UnmodifiableIterator;
import fj.F;
import fj.Unit;
import fj.data.List;
import fj.data.Option;

import java.util.Iterator;

/**
 * A topology is a collection instance that contains similar instances. It is
 * analogous to a list like data structure, except that the elements within the
 * topology may be subdivided into smaller <i>neighbourhoods</i>.
 *
 * @param <A> the type.
 * @author gpampara
 * @since 0.8
 */
public abstract class Topology<A> implements Iterable<A> {

    /**
     * Get the internally managed collection type.
     *
     * @return the internal list.
     */
    protected abstract List<A> delegate();

    /**
     * Given the provided {@code element}, return the neighborhood of
     * {@code element}.
     *
     * @param element neighborhood element to obtain neighborhood for
     * @return an {@code Iterator} for the returned neighborhood.
     */
    public abstract List<A> neighborhoodOf(A element);

    /**
     * Create a mutable buffer instance. Once the contents of the instances are
     * final, a new {@code Topology} instance may be created.
     *
     * @return a mutable buffer for the topology.
     */
    public abstract TopologyBuffer<A> newBuffer();

    /**
     * Returns an iterator over a set of elements of type T. The returned
     * {@code Iterator} is not modifiable - invoking {@link Iterator#remove()}
     * will throw a {@link UnsupportedOperationException}.
     *
     * @return an Iterator over the topology contents.
     */
    @Override
    public final Iterator<A> iterator() {
        final Iterator<A> iter = delegate().iterator();
        return new UnmodifiableIterator<A>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public A next() {
                return iter.next();
            }
        };
    }

    /**
     * Apply the provided function on each element within the current
     * {@code Topology}. The function {@code f} may result in side-effects.
     *
     * @param f the function to apply.
     */
    public final void foreach(F<A, Unit> f) {
        for (A a : delegate()) {
            f.f(a);
        }
    }

    /**
     * Reduce the elements contained, by applying the provided function. The
     * reduction is seeded with the provided initial value and is reduced by
     * folding the result of the function into the initial value, to the left.
     *
     * @param <B>     the return type.
     * @param f       the function to apply.
     * @param initial the initial value for the fold.
     * @return the result of the fold.
     */
    public final <B> B foldLeft(final F<B, F<A, B>> f, final B initial) {
        B x = initial;
        for (A element : delegate()) {
            x = f.f(x).f(element);
        }
        return x;
    }

    /**
     * Return the topology instance with the first {@code n} items removed.
     *
     * @param n the number of to drop.
     * @return the resulting topology
     */
    public abstract Topology<A> drop(int n);

    public abstract Topology<A> take(int n);

    /**
     * @param obj
     * @return
     * @TODO: This method needs to be tested / verified. Does it make sense?
     */
    public abstract Option<Integer> indexOf(A obj);

    /**
     * Definition of a builder for topology instances.
     *
     * @param <A> the type of the topology.
     */
    protected interface Buffer<A> {

        /**
         * Yield a new {@link Topology} instance from the builder.
         *
         * @return a new {@link Topology} instance.
         */
        Topology<A> build();

        /**
         * Add an element to the builder for inclusion in the final instance.
         *
         * @param element to be added.
         * @return the same {@linkplain TopologyBuilder builder} instance for chaining.
         */
        Buffer<A> add(A element);

        Buffer<A> addAll(List<A> list);
    }
}
