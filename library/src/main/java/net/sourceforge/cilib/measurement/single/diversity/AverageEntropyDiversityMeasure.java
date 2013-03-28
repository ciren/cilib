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
 * the average entropy over all dimensions in the search space.
 */
public class AverageEntropyDiversityMeasure extends Diversity {

    private EntropyMeasurement entropyMeasure;

    public AverageEntropyDiversityMeasure() {
        entropyMeasure = new DimensionalEntropyMeasurement();
    }

    public AverageEntropyDiversityMeasure(int intervals) {
        entropyMeasure = new DimensionalEntropyMeasurement(intervals);
    }

    public AverageEntropyDiversityMeasure(AverageEntropyDiversityMeasure copy) {
        this.entropyMeasure = copy.entropyMeasure;
    }

    @Override
    public AverageEntropyDiversityMeasure getClone() {
        return new AverageEntropyDiversityMeasure(this);
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
            diversity += ((Real) entropyMeasurements.get(i)).doubleValue();
        }

        diversity /= dimensions;

        diversity /= this.normalisationParameter.getNormalisationParameter(populationBasedAlgorithm);

        return Real.valueOf(diversity);
    }

    /**
     * Get the number of intervals over which entropy is measured.
     * @return The number of intervals over which entropy is measured.
     */
    public int getIntervals() {
        return entropyMeasure.getIntervals();
    }

    /**
     * Set the number of intervals over which entropy is measured.
     * @param intervals the number of intervals to set.
     */
    public void setIntervals(int intervals) {
        entropyMeasure.setIntervals(intervals);
    }

    public void setEntropyMeasure(EntropyMeasurement entropyMeasure) {
        this.entropyMeasure = entropyMeasure;
    }
}
