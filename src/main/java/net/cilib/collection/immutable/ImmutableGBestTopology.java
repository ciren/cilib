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
package net.cilib.collection.immutable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;
import net.cilib.collection.Topology;

/**
 * Implementation of the {@code g-best} topology. Each created topology
 * is completely immutable.
 * @param <A> The type.
 * @since 0.8
 * @author gpampara
 */
public class ImmutableGBestTopology<A> implements Topology<A> {

    private static final Topology<Object> INSTANCE = newBuilder().build();
    private final ImmutableList<A> elements;

    /**
     * Obtain an empty immutable topology.
     * @param <B> The defined type.
     * @return An immutable empty {@code g-best} topology.
     */
    public static <B> ImmutableGBestTopology<B> of() {
        return (ImmutableGBestTopology<B>) INSTANCE;
    }

    private ImmutableGBestTopology(List<A> elements) {
        this.elements = ImmutableList.copyOf(elements);
    }

    /**
     * Returns an iterator over a set of elements of type T. The returned
     * {@code Iterator} is not modifiable - invoking {@link Iterator#remove()}
     * will throw a {@link UnsupportedOperationException}.
     *
     * @return an Iterator over the topology contents.
     */
    @Override
    public Iterator<A> iterator() {
        final Iterator<A> iter = elements.iterator();
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
     * {@inheritDoc}
     */
    @Override
    public Iterator<A> neighborhoodOf(A element) {
        return iterator();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("contents", elements).toString();
    }

    /**
     * Obtain a {@code ImmutableTopologyBuilder} to build up a new topology
     * instance.
     * @param <A> The parameter type
     * @return an {@code ImmutableTopologyBuilder}.
     */
    public static <A> ImmutableGBestTopologyBuilder<A> newBuilder() {
        return new ImmutableGBestTopologyBuilder<A>();
    }

    /**
     * Topology builder to create ImmutableGBestTopology instances.
     * @param <B> the parameter type.
     */
    public static class ImmutableGBestTopologyBuilder<B> {

        private final List<B> elements;

        private ImmutableGBestTopologyBuilder() {
            elements = Lists.newArrayList();
        }

        /**
         * Construct an {@code ImmutableGBestTopology} from the contents of
         * the current builder instance. When obtaining the topology instance
         * from this builder, the builder is invalidated and all internal state
         * is cleared.
         * @return a new {@code ImmutableGBestTopology}.
         */
        public ImmutableGBestTopology<B> build() {
            try {
                return new ImmutableGBestTopology<B>(elements);
            } finally {
                elements.clear();
            }
        }

        /**
         * Add the provided element to the current builder, for inclusion in
         * the constructed topology instance.
         * @param element to add
         * @return the current topology builder for chaining purposes.
         */
        public ImmutableGBestTopologyBuilder<B> add(B element) {
            elements.add(element);
            return this;
        }
    }
}
