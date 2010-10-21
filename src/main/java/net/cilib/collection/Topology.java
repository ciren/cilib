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

import java.util.Iterator;

/**
 * @param <A>
 * @since 0.8
 * @author gpampara
 */
public interface Topology<A> extends Iterable<A> {

    /**
     * Given the provided {@code element}, return the neighborhood of
     * {@code element}.
     * @param element neighborhood element to obtain neighborhood for
     * @return an {@code Iterator} for the returned neighborhood.
     */
    Iterator<A> neighborhoodOf(A element);
}
