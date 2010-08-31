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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cilib.collection.immutable;

import java.util.Collections;
import java.util.Iterator;
import net.cilib.collection.Topology;
import net.cilib.collection.Topology.Builder;

/**
 *
 * @author gpampara
 */
final class EmptyImmutableTopology implements Topology<Object> {

    static final EmptyImmutableTopology INSTANCE = new EmptyImmutableTopology();

    private EmptyImmutableTopology() {
    }

    @Override
    public Builder<Object> newBuilder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Object> iterator() {
        return Collections.emptyList().iterator();
    }
}
