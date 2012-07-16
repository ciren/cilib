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
package net.sourceforge.cilib.algorithm;

import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Base interface defining all {@code Algorithm} classes.
 */
public interface Algorithm extends Runnable, Cloneable {

    /**
     * Perform the actions of the current {@linkplain Algorithm} for a single iteration.
     */
    void performIteration();

    /**
     * Perform the needed initialisation required before the execution of the algorithm
     * starts.
     */
    void performInitialisation();

    /**
     * Obtain the best current solution.
     * @return The {@code OptimisationSolution} representing the best solution.
     * @see net.sourceforge.cilib.entity.Topology#getBestEntity()
     */
    OptimisationSolution getBestSolution();

    /**
     * Obtain the collection of best solutions. This result does not actually make sense in normal
     * {@code PopulationBasedAlgorithm}s, but rather in a MultiObjective optimization.
     * @return An {@code Iterable} containing the solutions.
     */
    Iterable<OptimisationSolution> getSolutions();

    /**
     * Returns the number of iterations that have been performed by the algorihtm.
     * @return The number of iterations.
     *
     * Not Needed>??>????????????
     */
    int getIterations();

    /**
     * Set the optimisation problem to be solved. By default, the problem is <code>null</code>.
     * That is, it is necessary to set the optimisation problem before calling {@link #initialise()}.
     * @param problem An implementation of the
     * {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} interface.
     */
    void setOptimisationProblem(OptimisationProblem problem);

    /**
     * Get the specified {@linkplain OptimisationProblem}.
     * @return The specified {@linkplain OptimisationProblem}.
     */
    OptimisationProblem getOptimisationProblem();

    boolean isFinished();
}
