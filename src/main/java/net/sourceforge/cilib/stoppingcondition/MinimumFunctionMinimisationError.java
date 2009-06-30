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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 * @author Edwin Peer
 */
public class MinimumFunctionMinimisationError implements StoppingCondition {
    private static final long serialVersionUID = -7375489325180419208L;

    private Algorithm optAlgorithm;
    private double minimumError;
    private FunctionMinimisationProblem problem;

    /** Creates a new instance of MinimumErrorIndicator. */
    public MinimumFunctionMinimisationError() {
        minimumError = 1e-10;
    }

    public MinimumFunctionMinimisationError(double minimumError) {
        this.minimumError = minimumError;
    }

    public MinimumFunctionMinimisationError(MinimumFunctionMinimisationError copy) {
        this.minimumError = copy.minimumError;
        this.optAlgorithm = copy.optAlgorithm;
        this.problem = copy.problem;
    }

    public MinimumFunctionMinimisationError getClone() {
        return new MinimumFunctionMinimisationError(this);
    }

    public void setError(double minimumError) {
        this.minimumError = minimumError;
    }

    public double getError() {
        return minimumError;
    }

    public double getPercentageCompleted() {
        double error = problem.getError(optAlgorithm.getBestSolution().getPosition());
        if (error <= minimumError) {
            return 1;
        }
        return minimumError / error;
    }

    public boolean isCompleted() {
        return problem.getError(optAlgorithm.getBestSolution().getPosition()) <= minimumError;
    }

    public void setAlgorithm(Algorithm algorithm) {
        optAlgorithm = algorithm;
        problem = (FunctionMinimisationProblem) optAlgorithm.getOptimisationProblem();
    }
}
