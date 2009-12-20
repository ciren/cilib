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

/**
 * @author Edwin Peer
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
