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
package net.cilib.algorithm;

import net.cilib.collection.Topology;

/**
 * Algorithm that operates on a {@link Topology}.
 *
 * @todo A Question arises regarding the use of the generic parameter here.
 *
 * @param <A>
 * @since 0.8
 * @author gpampara
 */
public abstract class PopulationBasedAlgorithm<A> implements Algorithm {

    /**
     * Perform an iteration of the population based algorithm. The provided
     * {@code Topology} instance is not modified and a new {@code Topology}
     * is then returned.
     * @param topology the population for the algorithm to operate on
     * @return the given topology, post algorithm iteration.
     */
    public abstract Topology<A> next(Topology<A> topology);
}
