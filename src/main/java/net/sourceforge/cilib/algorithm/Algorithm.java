/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.algorithm;

import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Base interface defining all {@code Algorithm} classes.
 */
public interface Algorithm extends Cloneable {

    /**
     * Perform the actions of the current {@linkplain Algorithm} for a single iteration.
     */
    public void performIteration();

    /**
     * Perform the needed initialisation required before the execution of the algorithm
     * starts.
     */
    public void performInitialisation();

    /**
     * Perform the needed unintialisation steps after the algorithm completes it's
     * execution.
     */
    public void performUninitialisation();

    /**
     * Obtain the best current solution.
     * @return The {@code OptimisationSolution} representing the best solution.
     * @see net.sourceforge.cilib.entity.Topology#getBestEntity()
     */
    public OptimisationSolution getBestSolution();

    /**
     * Obtain the collection of best solutions. This result does not actually make sense in normal
     * {@code PopulationBasedAlgorithm}s, but rather in a MultiObjective optimization.
     * @return An {@code Iterable} containing the solutions.
     */
    public Iterable<OptimisationSolution> getSolutions();

    /**
     * Returns the number of iterations that have been performed by the algorihtm.
     * @return The number of iterations.
     *
     * Not Needed>??>????????????
     */
     public int getIterations();

    /**
     * Set the optimisation problem to be solved. By default, the problem is <code>null</code>.
     * That is, it is necessary to set the optimisation problem before calling {@link #initialise()}.
     * @param problem An implementation of the
     * {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} interface.
     */
    public void setOptimisationProblem(OptimisationProblem problem);

    /**
     * Get the specified {@linkplain OptimisationProblem}.
     * @return The specified {@linkplain OptimisationProblem}.
     */
    public OptimisationProblem getOptimisationProblem();
}
