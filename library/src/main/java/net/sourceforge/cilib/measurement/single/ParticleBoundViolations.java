/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.Iterator;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

/**
 * Calculates the average number of particles in the current swarm that
 * violates boundary constraints. This measure can be used as an
 * indicator of whether the algorithm spend too much time exploring
 * in infeasible space (with respect to the boundary constraints).
 *
 */
public class ParticleBoundViolations implements Measurement<Real> {

    private static final long serialVersionUID = 2232130008790333636L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurement getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        Iterator<Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();

        int numberOfViolations = 0;
        int populationSize = populationBasedAlgorithm.getTopology().length();

        while (populationIterator.hasNext()) {
            Entity entity = populationIterator.next();

            Iterator positionIterator = entity.getPosition().iterator();

            while (positionIterator.hasNext()) {
                Numeric position = (Numeric) positionIterator.next();
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
