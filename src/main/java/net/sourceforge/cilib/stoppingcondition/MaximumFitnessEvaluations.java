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
public class MaximumFitnessEvaluations implements StoppingCondition {
    private static final long serialVersionUID = 92433928310230011L;

    private int maximumFitnessEvaluations;
    private Algorithm algorithm;

    /** Creates a new instance of MaximumFitnessEvaluationsIndicator. */
    public MaximumFitnessEvaluations() {
        maximumFitnessEvaluations = 200000;
    }

    public MaximumFitnessEvaluations(MaximumFitnessEvaluations copy) {
        this.maximumFitnessEvaluations = copy.maximumFitnessEvaluations;
        this.algorithm = copy.algorithm;
    }

    public MaximumFitnessEvaluations getClone() {
        return new MaximumFitnessEvaluations(this);
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

    public double getPercentageCompleted() {
        return ((double) algorithm.getOptimisationProblem().getFitnessEvaluations()) / ((double) maximumFitnessEvaluations);
    }

    public boolean isCompleted() {
        return algorithm.getOptimisationProblem().getFitnessEvaluations() >= maximumFitnessEvaluations;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
