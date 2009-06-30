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

import net.sourceforge.cilib.problem.GradientOptimisationProblem;

/**
 * Enable the notion of a gradient based optimisation. All {@code Algorithm}s that can
 * be used to optimise a gradient based problem should implement this interface.
 * @author  Edwin Peer
 */
public interface GradientOptimisationAlgorithm {
     /**
     * Sets the optimisation problem to be solved.
     *
     * @param problem The {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} to be solved.
     */
    public void setGradientOptimisationProblem(GradientOptimisationProblem problem);

    /**
     * Accessor for the optimisation problem to be solved.
     *
     * @return The {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} to be solved.
     */
    public GradientOptimisationProblem getGradientOptimisationProblem();

    /**
     * Get the number of evaluations that have been performed to determine the gradient.
     * @return The number of gradient evaluations.
     */
    public int getGradientEvaluations();

}
