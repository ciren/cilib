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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 */
public class BestSolutionImprovement implements Measurement<Real> {

    private static final long serialVersionUID = 7547646366505677446L;
    private Double previousBestSolutionFitness;

    public BestSolutionImprovement() {
        this.previousBestSolutionFitness = Double.NaN;
    }

    public BestSolutionImprovement(BestSolutionImprovement copy) {
        this.previousBestSolutionFitness = copy.previousBestSolutionFitness;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BestSolutionImprovement getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        double currentBestSolutionFitness = algorithm.getBestSolution().getFitness().getValue();
        double difference = currentBestSolutionFitness;
        if(!previousBestSolutionFitness.isNaN()) {
            difference = previousBestSolutionFitness - currentBestSolutionFitness; // signed on purpose! negative sign indicates worsened fitness
        }
        previousBestSolutionFitness =currentBestSolutionFitness;
        return Real.valueOf(difference);
    }
}
