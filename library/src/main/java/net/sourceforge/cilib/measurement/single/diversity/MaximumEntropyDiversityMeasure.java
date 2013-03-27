/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.entropy.DimensionalEntropyMeasurement;
import net.sourceforge.cilib.measurement.entropy.EntropyMeasurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * A diversity measurement that calculates the diversity of a population to be
 * the maximum of entropy measurements over all dimensions in the search space.
 *
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

        diversity /= this.normalisationParameter.getNormalisationParameter(populationBasedAlgorithm);

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
     * @param intervals the number of intervals to set.
     */
    public void setIntervals(int intervals) {
        entropyMeasure.setIntervals(intervals);
    }

    public void setEntropyMeasure(EntropyMeasurement entropyMeasure) {
        this.entropyMeasure = entropyMeasure;
    }
}
