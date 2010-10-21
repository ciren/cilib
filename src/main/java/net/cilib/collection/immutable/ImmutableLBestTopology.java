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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.cilib.collection.Topology;

/**
 * @param <A>
 * @since 0.8
 * @author gpampara
 */
public class ImmutableLBestTopology<A> implements Topology<A> {

    private static final Topology<Object> INSTANCE = new ImmutableLBestTopology<Object>(Lists.newArrayList());
    private final ImmutableList<A> elements;
    private final int neighborhoodsize;

    public static <B> ImmutableLBestTopology<B> of() {
        return (ImmutableLBestTopology<B>) INSTANCE;
    }

    private ImmutableLBestTopology(List<A> elements) {
        this.elements = ImmutableList.copyOf(elements);
        this.neighborhoodsize = 3;
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
                if (indexes.contains(index)) {
                    return true;
                }
                return false;
            }
        });
        return internal.iterator();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("contents", elements).toString();
    }

    public static <A> ImmutableTopologyBuilder<A> newBuilder() {
        return new ImmutableTopologyBuilder<A>();
    }

    public static class ImmutableTopologyBuilder<B> {

        private final List<B> elements;

        ImmutableTopologyBuilder() {
            elements = Lists.newArrayList();
        }

        public ImmutableLBestTopology<B> build() {
            try {
                return new ImmutableLBestTopology<B>(elements);
            } finally {
                elements.clear();
            }
        }

        public ImmutableTopologyBuilder<B> add(B element) {
            elements.add(element);
            return this;
        }
    }
}
