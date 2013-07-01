/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.single;

import net.cilib.algorithm.Algorithm;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.entity.Entity;
import net.cilib.measurement.Measurement;
import net.cilib.type.types.Bounds;
import net.cilib.type.types.Numeric;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;

/**
 * Calculates the average number of violations of boundary constraints
 * with respect to each dimension. This measure can be used as an
 * indicator of whether the algorithm spend too much time exploring
 * in infeasible space (with respect to the boundary constraints).
 *
 */
public class DimensionBoundViolationsPerParticle implements Measurement<Real> {

    private static final long serialVersionUID = -3633155366562479197L;

    /** Creates a new instance of DimensionBoundViolationsPerParticle. */
    public DimensionBoundViolationsPerParticle() {
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public DimensionBoundViolationsPerParticle(DimensionBoundViolationsPerParticle copy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DimensionBoundViolationsPerParticle getClone() {
        return new DimensionBoundViolationsPerParticle(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;
        double sumOfAverageViolations = 0.0;
        int populationSize = populationBasedAlgorithm.getTopology().length();
        int numberOfViolations;
        int dimension = 0;

        fj.data.List<Entity> local = populationBasedAlgorithm.getTopology();
        for (Entity populationEntity : local) {
            numberOfViolations = 0;
            dimension = populationEntity.getDimension();

            for (Numeric position : (Vector) populationEntity.getCandidateSolution()) {
                Bounds bounds = position.getBounds();

                if (!bounds.isInsideBounds(position.doubleValue())) {
                    numberOfViolations++;
                }
            }
            sumOfAverageViolations += numberOfViolations;
        }

        return Real.valueOf(sumOfAverageViolations / (double) populationSize);
    }
}
