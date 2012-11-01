/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

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
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;

        int numberOfViolations = 0;
        int populationSize = populationBasedAlgorithm.getTopology().size();

        for (Entity populationEntity : populationBasedAlgorithm.getTopology()) {
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
