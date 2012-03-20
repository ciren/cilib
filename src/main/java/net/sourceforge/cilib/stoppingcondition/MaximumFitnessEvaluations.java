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

/**
 */
public class MaximumFitnessEvaluations implements StoppingCondition<Algorithm> {
    private static final long serialVersionUID = 92433928310230011L;

    private int maximumFitnessEvaluations;

    /** Creates a new instance of MaximumFitnessEvaluationsIndicator. */
    public MaximumFitnessEvaluations() {
        maximumFitnessEvaluations = 200000;
    }

    public MaximumFitnessEvaluations(int maximumFitnessEvaluations) {
        this.maximumFitnessEvaluations = maximumFitnessEvaluations;
    }

    public void setMaximumFitnessEvaluations(int maximumFitnessEvaluations) {
        this.maximumFitnessEvaluations = maximumFitnessEvaluations;
    }

    public int getMaximumFitnessEvaluations() {
        return maximumFitnessEvaluations;
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        return ((double) algorithm.getOptimisationProblem().getFitnessEvaluations()) / ((double) maximumFitnessEvaluations);
    }

    @Override
    public boolean apply(Algorithm input) {
        return input.getOptimisationProblem().getFitnessEvaluations() >= maximumFitnessEvaluations;
    }

}
