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
import net.cilib.collection.TopologyBuffer;

/**
 * Implementation of the {@code g-best} topology. Each created topology
 * is completely immutable.
 * @param <A> The type.
 * @since 0.8
 * @author gpampara
 */
public class ImmutableGBestTopology<A> implements Topology<A> {

    private static final ImmutableGBestTopology<Object> INSTANCE = new ImmutableGBestTopology<Object>(Lists.newArrayList());
    private final ImmutableList<A> elements;

    /**
     * Obtain an empty immutable topology.
     * @param <B> type parameter.
     * @return An immutable empty {@code g-best} topology.
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Provide the {@code String} representation of the topology.
     * @return {@code String} representation.
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("contents", elements).toString();
    }

    @Override
    public TopologyBuffer<A> newBuffer() {
        return new TopologyBuffer<A>(new ImmutableGBestTopologyBuilder<A>(), ImmutableList.<A>of());
    }

    /**
     * Create a new topology instance, with the given elements.
     * @param <A> The type
     * @param first the first element for the topology.
     * @param rest the "potential" remainder of the topology.
     * @return a new {@code ImmutableGBestTopology} instance containing the given
     *         elements.
     */
    public static <A> Topology<A> topologyOf(A first, A... rest) {
        ImmutableGBestTopologyBuilder<A> builder = new ImmutableGBestTopologyBuilder<A>();
        builder.add(first);
        for (A a : rest) {
            builder.add(a);
        }
        return builder.build();
    }

    /**
     * Topology builder to create ImmutableGBestTopology instances.
     * @param <B> parameter type.
     */
    public static class ImmutableGBestTopologyBuilder<B> implements TopologyBuilder<B> {

        private final List<B> elements;

        public ImmutableGBestTopologyBuilder() {
            elements = Lists.newArrayList();
        }

        /**
         * Construct an {@code ImmutableGBestTopology} from the contents of
         * the current builder instance. When obtaining the topology instance
         * from this builder, the builder is invalidated and all internal state
         * is cleared.
         * @return a new {@code ImmutableGBestTopology}.
         */
        @Override
        public Topology<B> build() {
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
        @Override
        public TopologyBuilder<B> add(B element) {
            elements.add(element);
            return this;
        }
    }
}
