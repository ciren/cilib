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
package net.sourceforge.cilib.algorithm.population;

import java.io.Serializable;

import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface to define the manner in which an iteration is defined for a
 * {@code PopulationBasedAlgorithm}.
 *
 * @param <E> The {@code PopulationBasedAlgorithm} type.
 */
public interface IterationStrategy<E extends PopulationBasedAlgorithm> extends Cloneable, Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    abstract IterationStrategy<E> getClone();

    /**
     * Perform the iteration of the PopulationBasedAlgorithm.
     * <p>
     * Due to the nature of the PopulationBasedAlgorithms, the actual manner in which the algorithm's
     * iteration is performed is deferred to the specific iteration strategy being used.
     * <p>
     * This implies that the general structure of the iteration for a specific flavour of
     * algorithm is constant with modifications on that algorithm being made. For example,
     * within a Genetic Algorithm you would expect:
     * <ol>
     *   <li>Parent individuals to be <b>selected</b> in some manner</li>
     *   <li>A <b>crossover</b> process to be done on the selected parent individuals to create
     *       the offspring</li>
     *   <li>A <b>mutation</b> process to alter the generated offspring</li>
     *   <li><b>Recombine</b> the existing parent individuals and the generated offspring to create
     *       the next generation</li>
     * </ol>
     *
     * @param algorithm The algorithm to perform the iteration process on.
     */
    abstract void performIteration(E algorithm);

    /**
     * Get the currently associated {@linkplain BoundaryConstraint}.
     * @return The current {@linkplain BoundaryConstraint}.
     */
    BoundaryConstraint getBoundaryConstraint();

    /**
     * Set the {@linkplain BoundaryConstraint} to maintain within this {@linkplain IterationStrategy}.
     * @param boundaryConstraint The {@linkplain BoundaryConstraint} to set.
     */
    void setBoundaryConstraint(BoundaryConstraint boundaryConstraint);
}
