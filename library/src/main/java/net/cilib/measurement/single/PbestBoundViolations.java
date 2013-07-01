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
import net.cilib.entity.EntityType;
import net.cilib.measurement.Measurement;
import net.cilib.type.types.Bounds;
import net.cilib.type.types.Numeric;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;

/**
 * Calculates the average number of personal best positions in
 * the current swarm that violates boundary constraints.
 *
 */
public class PbestBoundViolations implements Measurement<Real> {

    private static final long serialVersionUID = 7547646366505677446L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PbestBoundViolations getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        int numberOfViolations = 0;
        int populationSize = populationBasedAlgorithm.getTopology().length();
        fj.data.List<Entity> local = populationBasedAlgorithm.getTopology();
        for (Entity populationEntity : local) {
            Vector pbest = (Vector) populationEntity.getProperties().get(EntityType.Particle.BEST_POSITION);
            if (pbest == null) {
                throw new UnsupportedOperationException("Entity is not a particle.");
            }

            for (Numeric position : pbest) {
                Bounds bounds = position.getBounds();

                if (!bounds.isInsideBounds(position.doubleValue())) {
                    numberOfViolations++;
                    break;
                }
            }
        }

        return Real.valueOf((double) numberOfViolations / (double) populationSize);
    }
}
