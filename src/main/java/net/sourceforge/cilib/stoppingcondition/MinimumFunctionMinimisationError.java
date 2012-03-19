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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 */
public class MinimumFunctionMinimisationError implements StoppingCondition<Algorithm> {

    private static final long serialVersionUID = -7375489325180419208L;
    private double minimumError;

    /** Creates a new instance of MinimumErrorIndicator. */
    public MinimumFunctionMinimisationError() {
        minimumError = 1e-10;
    }

    public MinimumFunctionMinimisationError(double minimumError) {
        this.minimumError = minimumError;
    }

    public void setError(double minimumError) {
        this.minimumError = minimumError;
    }

    public double getError() {
        return minimumError;
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        FunctionMinimisationProblem problem = (FunctionMinimisationProblem) algorithm.getOptimisationProblem();
        double error = problem.getError(algorithm.getBestSolution().getPosition());
        if (error <= minimumError) {
            return 1;
        }
        return minimumError / error;
    }

    @Override
    public boolean apply(Algorithm input) {
        FunctionMinimisationProblem problem = (FunctionMinimisationProblem) input.getOptimisationProblem();
        return problem.getError(input.getBestSolution().getPosition()) <= minimumError;
    }
}
