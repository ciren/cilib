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
package net.sourceforge.cilib.measurement.single.diversity;

import net.sourceforge.cilib.measurement.entropy.DimensionalEntropyMeasurement;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.entropy.EntropyMeasurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * A diversity measurement that calculates the diversity of a population to be
 * the maximum of entropy measurements over all dimensions in the search space.
 *
 * @author Bennie Leonard
 */
public class MaximumEntropyDiversityMeasure extends Diversity {

    private EntropyMeasurement entropyMeasure;

    public MaximumEntropyDiversityMeasure() {
        entropyMeasure = new DimensionalEntropyMeasurement();
    }

    public MaximumEntropyDiversityMeasure(int intervals) {
        entropyMeasure = new DimensionalEntropyMeasurement(intervals);
    }

    public MaximumEntropyDiversityMeasure(MaximumEntropyDiversityMeasure copy) {
        this.entropyMeasure = copy.entropyMeasure;
    }

    @Override
    public MaximumEntropyDiversityMeasure getClone() {
        return new MaximumEntropyDiversityMeasure(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;
        int dimensions = populationBasedAlgorithm.getOptimisationProblem().getDomain().getDimension();

        double diversity = 0.0;
        TypeList entropyMeasurements = entropyMeasure.getValue(algorithm);

        for (int i = 0; i < dimensions; i++) {
            double value = ((Real) entropyMeasurements.get(i)).doubleValue();
            if (diversity < value) {
                diversity = value;
            }
        }

        diversity /= this.normalisationParameter.getValue();

        return Real.valueOf(diversity);
    }

    /**
     * Convenience method to get the number of intervals over which
     * entropy is measured.
     * @return The number of intervals over which entropy is measured.
     */
    public int getIntervals() {
        return entropyMeasure.getIntervals();
    }

    /**
     * Convenience method to set the number of intervals over which
     * entropy is measured.
     * @param i the number of intervals to set.
     */
    public void setIntervals(int intervals) {
        entropyMeasure.setIntervals(intervals);
    }
}
