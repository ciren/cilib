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

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.UnconstrainedBoundary;

/**
 * Generic {@code IterationStrategy} class for all population based algorithms. Each and every {@link IterationStrategy}
 * will have a pipeline available to it in order to specify the needed operators that will be
 * required to be executed during the iteration. For the implementing classes, a default
 * pipeline is constructed and made available, this can (as with everything within CIlib) be overridden.
 *
 *
 * @param <E> The {@linkplain PopulationBasedAlgorithm} type.
 */
public abstract class AbstractIterationStrategy<E extends Algorithm> implements IterationStrategy<E> {

    private static final long serialVersionUID = -2922555178733552167L;
    protected BoundaryConstraint boundaryConstraint;

    /**
     * Create an instance of the {@linkplain IterationStrategy}.
     */
    public AbstractIterationStrategy() {
        this.boundaryConstraint = new UnconstrainedBoundary();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractIterationStrategy<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void performIteration(E algorithm);

    /**
     * Get the currently associated {@linkplain BoundaryConstraint}.
     * @return The current {@linkplain BoundaryConstraint}.
     */
    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return boundaryConstraint;
    }

    /**
     * Set the {@linkplain BoundaryConstraint} to maintain within this {@linkplain IterationStrategy}.
     * @param boundaryConstraint The {@linkplain BoundaryConstraint} to set.
     */
    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.boundaryConstraint = boundaryConstraint;
    }
}
