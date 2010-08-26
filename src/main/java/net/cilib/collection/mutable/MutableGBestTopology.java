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

import java.util.Iterator;
import net.cilib.collection.Topology;

/**
 * @since 0.8
 * @author gpampara
 */
public class MutableGBestTopology<A> implements Topology<A> {

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public A next() {
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    public void add(A select) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
