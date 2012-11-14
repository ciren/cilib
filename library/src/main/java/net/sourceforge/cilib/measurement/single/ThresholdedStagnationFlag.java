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
import net.sourceforge.cilib.type.types.Bit;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 */
public class ThresholdedStagnationFlag implements Measurement<Bit> {

    private static final long serialVersionUID = 7547646366505677446L;
    private BestSolutionImprovement bestSolutionImprovement;
    private int iterationThreshold = 5;
    private int iterationCount;
    private double epsilon = 0.00001;

    public ThresholdedStagnationFlag() {
        this.bestSolutionImprovement = new BestSolutionImprovement();
    }
    
    public ThresholdedStagnationFlag(int iterationThreshold, double epsilon) {
        this.iterationThreshold = iterationThreshold;
        this.epsilon = epsilon;
    }

    public ThresholdedStagnationFlag(ThresholdedStagnationFlag copy) {
        this.bestSolutionImprovement = copy.bestSolutionImprovement.getClone();
        this.iterationThreshold = copy.iterationThreshold;
        this.epsilon = copy.epsilon;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ThresholdedStagnationFlag getClone() {
        return new ThresholdedStagnationFlag(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bit getValue(Algorithm algorithm) {
        if(bestSolutionImprovement.getValue(algorithm).doubleValue() < epsilon) {
            iterationCount++;
        }
        else {
            iterationCount = 0;
        }
        if(iterationCount >= this.iterationThreshold) {
            iterationCount = 0;
            return Bit.valueOf(true);
        }
        else {
            return Bit.valueOf(false);
        }
    }

    public int getIterationThreshold() {
        return iterationThreshold;
    }

    public void setIterationThreshold(int iterationThreshold) {
        this.iterationThreshold = iterationThreshold;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
