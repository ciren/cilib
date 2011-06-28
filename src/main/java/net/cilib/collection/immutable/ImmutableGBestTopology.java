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
import fj.Equal;
import fj.data.List;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.collection.TopologyBuffer;

/**
 * Implementation of the {@code g-best} topology. Each created topology
 * is completely immutable.
 *
 * @param <A> The type.
 * @author gpampara
 * @since 0.8
 */
public class ImmutableGBestTopology<A> extends Topology<A> {

    private static final ImmutableGBestTopology<Object> INSTANCE = new ImmutableGBestTopology<Object>(List.<Object>nil());
    private final List<A> elements;

    /**
     * Obtain an empty immutable topology.
     *
     * @param <B> type parameter.
     * @return An immutable empty {@code g-best} topology.
     */
    @SuppressWarnings("unchecked")
    public static <B> ImmutableGBestTopology<B> of() {
        return (ImmutableGBestTopology<B>) INSTANCE;
    }

    private ImmutableGBestTopology(List<A> elements) {
        this.elements = elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<A> neighborhoodOf(A element) {
        return elements;
    }

    /**
     * Provide the {@code String} representation of the topology.
     *
     * @return {@code String} representation.
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("contents", elements).toString();
    }

    /**
     * Create a mutable buffer that my be finalized into a {@link ImmutableGBestTopology}
     * instance.
     *
     * @return A new {@link TopologyBuffer} instance that can construct an
     *         {@link ImmutableGBestTopology} instance.
     * @see TopologyBuffer
     */
    @Override
    public TopologyBuffer<A> newBuffer() {
        return new TopologyBuffer<A>(new ImmutableGBestTopologyBuffer<A>());
    }

    @Override
    public Topology<A> drop(int n) {
        return new ImmutableGBestTopology<A>(this.elements.drop(n));
    }

    @Override
    public Topology<A> take(int n) {
        return new ImmutableGBestTopology<A>(this.elements.take(n));
    }

    @Override
    public Option<Integer> indexOf(A obj) {
        return this.elements.elementIndex(Equal.<A>anyEqual(), obj);
    }

    /**
     * Create a new topology instance, with the given elements.
     *
     * @param <A>   The type
     * @param first the first element for the topology.
     * @param rest  the "potential" remainder of the topology.
     * @return a new {@code ImmutableGBestTopology} instance containing the given
     *         elements.
     */
    public static <A> ImmutableGBestTopology<A> topologyOf(A first, A... rest) {
        ImmutableGBestTopologyBuffer<A> builder = new ImmutableGBestTopologyBuffer<A>();
        builder.add(first);
        for (A a : rest) {
            builder.add(a);
        }
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<A> delegate() {
        return this.elements;
    }

    /**
     * Topology builder to create ImmutableGBestTopology instances.
     *
     * @param <B> parameter type.
     */
    public static class ImmutableGBestTopologyBuffer<B> implements Buffer<B> {

        private List.Buffer<B> elements;

        public ImmutableGBestTopologyBuffer() {
            elements = List.Buffer.empty();
        }

        /**
         * Construct an {@code ImmutableGBestTopology} from the contents of
         * the current builder instance. When obtaining the topology instance
         * from this builder, the builder is invalidated and all internal state
         * is cleared.
         *
         * @return a new {@code ImmutableGBestTopology}.
         */
        @Override
        public ImmutableGBestTopology<B> build() {
            try {
                return new ImmutableGBestTopology<B>(elements.toList());
            } finally {
                elements = List.Buffer.empty();
            }
        }

        /**
         * Add the provided element to the current builder, for inclusion in
         * the constructed topology instance.
         *
         * @param element to add
         * @return the current topology builder for chaining purposes.
         */
        @Override
        public Buffer<B> add(B element) {
            elements.snoc(element);
            return this;
        }

        @Override
        public Buffer<B> addAll(List<B> list) {
            elements.append(list);
            return this;
        }
    }
}
