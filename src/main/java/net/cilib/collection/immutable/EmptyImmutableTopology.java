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
import java.util.Collections;
import java.util.Iterator;
import net.cilib.collection.Topology;
import net.cilib.collection.Topology.TopologyBuilder;
import net.cilib.collection.TopologyBuffer;

/**
 *
 * @author gpampara
 */
final class EmptyImmutableTopology implements Topology<Object> {

    static final EmptyImmutableTopology INSTANCE = new EmptyImmutableTopology();

    private EmptyImmutableTopology() {
    }

    /**
     * Return an {@code Iterator} over an empty list of elements.
     * @return an empty {@code Iterator} instance.
     */
    @Override
    public Iterator<Object> iterator() {
        return Collections.emptyList().iterator();
    }

    /**
     * {@inheritDoc}
     *
     * The neighborhood is defined to not contain any elements.
     * @see EmptyImmutableTopology#iterator()
     */
    @Override
    public Iterator<Object> neighborhoodOf(Object element) {
        return iterator();
    }

    @Override
    public TopologyBuffer<Object> newBuffer() {
        return new TopologyBuffer<Object>(new EmptyTopologyBuilder(), ImmutableList.of());
    }

    private static class EmptyTopologyBuilder implements Topology.TopologyBuilder<Object> {

        @Override
        public Topology<Object> build() {
            return INSTANCE;
        }

        @Override
        public TopologyBuilder<Object> add(Object element) {
            return this;
        }
    }
}
