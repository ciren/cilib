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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;
import net.cilib.collection.Topology;

/**
 * @param <A>
 * @since 0.8
 * @author gpampara
 */
public class ImmutableGBestTopology<A> implements Topology<A> {

    private static final Topology<Object> INSTANCE = new ImmutableGBestTopology<Object>(Lists.newArrayList());
    private final ImmutableList<A> elements;

    public static <B> ImmutableGBestTopology<B> of() {
        return (ImmutableGBestTopology<B>) INSTANCE;
    }

    private ImmutableGBestTopology(List<A> elements) {
        this.elements = ImmutableList.copyOf(elements);
    }

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

    @Override
    public Builder<A> newBuilder() {
        return new ImmutableTopologyBuilder<A>();
    }

    private static class ImmutableTopologyBuilder<B> implements Topology.Builder<B> {

        private final List<B> elements;

        ImmutableTopologyBuilder() {
            elements = Lists.newArrayList();
        }

        @Override
        public Topology<B> build() {
            return new ImmutableGBestTopology<B>(elements);
        }

        @Override
        public boolean add(B element) {
            return elements.add(element);
        }
    }
}
