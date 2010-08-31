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
package net.cilib.collection.mutable;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;
import net.cilib.collection.Topology;
import net.cilib.collection.Topology.Builder;

/**
 * @param <A> The element type.
 * @since 0.8
 * @author gpampara
 */
public class MutableGBestTopology<A> implements Topology<A> {
    private final List<A> internal;

    public MutableGBestTopology() {
        this.internal = Lists.newArrayList();
    }

    @Override
    public Iterator<A> iterator() {
        final Iterator<A> iter = internal.iterator();
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

    public void add(A select) {
        internal.add(select);
    }

    @Override
    public Builder<A> newBuilder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
