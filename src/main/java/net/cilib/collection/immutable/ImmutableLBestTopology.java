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
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.cilib.collection.Topology;
import net.cilib.collection.TopologyBuffer;

/**
 * {@code l-best} topology implementation. The {@code l-best} topology defines
 * a neighborhood size and is per default set to a value of 3; the neighborhood
 * size may change, when using the defined topology builder.
 * @param <A> the parameter type.
 * @since 0.8
 * @author gpampara
 */
public class ImmutableLBestTopology<A> implements Topology<A> {

    private static final Topology<Object> INSTANCE = new ImmutableLBestTopology<Object>(Lists.newArrayList(), 0);
    private final ImmutableList<A> elements;
    private final int neighborhoodsize;

    /**
     * Obtain an empty immutable topology.
     * @return An immutable empty {@code l-best} topology.
     */
    public static <B> ImmutableLBestTopology<B> of() {
        return (ImmutableLBestTopology<B>) INSTANCE;
    }

    private ImmutableLBestTopology(List<A> elements, int size) {
        this.elements = ImmutableList.copyOf(elements);
        this.neighborhoodsize = size;
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
     * Return an {@code iterator} for the neighborhood of the given element.
     * @param element queried for it's neighborhood
     * @return an {@code iterator} to the neighborhood of {@code element}
     */
    @Override
    public Iterator<A> neighborhoodOf(final A element) {
        final List<Integer> indexes = Lists.newArrayListWithCapacity(neighborhoodsize);
        int start = elements.indexOf(element) - ((neighborhoodsize - 1) / 2);
        start = (start < 0) ? start += elements.size() : start;

        for (int i = 0; i < neighborhoodsize; i++) {
            indexes.add((start + i) % elements.size());
        }

        Collection<A> internal = Collections2.filter(elements, new Predicate<A>() {
            @Override
            public boolean apply(A input) {
                int index = elements.indexOf(input);
                return indexes.contains(index);
            }
        });
        return internal.iterator();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("contents", elements).toString();
    }

    /**
     * Create a new topology instance, with the given elements.
     * @param <A> The type
     * @param neighborhoodSize the size of neighborhoods within the topology.
     * @param first the first element for the topology.
     * @param rest the "potential" remainder of the topology.
     * @return a new {@code ImmutableLBestTopology} instance containing the given
     *         elements.
     */
    public static <A> ImmutableLBestTopology<A> topologyOf(int neighborhoodSize, A first, A... rest) {
        ImmutableLBestTopologyBuilder<A> builder = new ImmutableLBestTopologyBuilder<A>();
        builder.withNeighborhoodSize(neighborhoodSize).add(first);
        for (A a : rest) {
            builder.add(a);
        }
        return builder.build();
    }

    @Override
    public TopologyBuffer<A> newBuffer() {
        return new TopologyBuffer<A>(new ImmutableLBestTopologyBuilder<A>(), ImmutableList.<A>of());
    }

    /**
     * Create a topology builder to create {@code ImmutableLBestTopology}
     * instances. The default neighborhood size is defined to be {@code 3}.
     */
    public static class ImmutableLBestTopologyBuilder<B> implements Topology.TopologyBuilder<B> {

        private final List<B> elements;
        private int neighborhoodSize = 3;

        ImmutableLBestTopologyBuilder() {
            elements = Lists.newArrayList();
        }

        /**
         * Construct an {@code ImmutableLBestTopology} from the contents of
         * the current builder instance. When obtaining the topology instance
         * from this builder, the builder is invalidated and all internal state
         * is cleared.
         * @return a new {@code ImmutableLBestTopology}.
         */
        @Override
        public ImmutableLBestTopology<B> build() {
            try {
                return new ImmutableLBestTopology<B>(elements, neighborhoodSize);
            } finally {
                elements.clear();
                neighborhoodSize = 3;
            }
        }

        /**
         * Add the provided element to the current builder, for inclusion in
         * the constructed topology instance.
         * @param element to add
         * @return the current topology builder for chaining purposes.
         */
        @Override
        public ImmutableLBestTopologyBuilder<B> add(B element) {
            elements.add(element);
            return this;
        }

        /**
         * Define the neighborhood size for the {@code ImmutableLBestTopology}
         * to be built from this builder.
         * @param size the neighborhood size.
         * @return the current builder instance.
         */
        public ImmutableLBestTopologyBuilder<B> withNeighborhoodSize(int size) {
            Preconditions.checkArgument(neighborhoodSize >= 3, "Minimum required neighborhood size is 3.");
            this.neighborhoodSize = size;
            return this;
        }
    }
}
