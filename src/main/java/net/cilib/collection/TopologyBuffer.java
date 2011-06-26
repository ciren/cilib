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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * @param <A>
 */
public class TopologyBuffer<A> implements Iterable<A> {
    private Topology.Buffer<A> builder;
    private final List<A> inner;

    public TopologyBuffer(Topology.Buffer<A> builder/*, ImmutableList<A> list*/) {
        this.builder = builder;
        this.inner = Lists.newArrayList();
    }

    /**
     * @param element
     * @return
     */
    public boolean add(A element) {
        return this.inner.add(element);
    }

    public TopologyBuffer<A> addAll(Iterable<A> iterable) {
        Iterables.addAll(this.inner, iterable);
        return this;
    }

    /**
     * @return
     */
    public Topology<A> build() {
        try {
            for (A a : inner) {
                builder.add(a);
            }
            return builder.build();
        } finally {
            builder = null;
        }
    }

    @Override
    public Iterator<A> iterator() {
        return inner.iterator();
    }
}
