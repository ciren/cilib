/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class currently only works for PSO, it finds the best personal best fitness
 * values for each population in a multi-population based algorithm without re-calculating
 * the fitness.
 */
public class MultiPopulationFitness implements Measurement<Vector> {

    private static final long serialVersionUID = -608120128187899491L;

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
    public Vector getValue(Algorithm algorithm) {
        Vector.Builder fitness = Vector.newBuilder();
        MultiPopulationBasedAlgorithm ca = (MultiPopulationBasedAlgorithm) algorithm;
        for (SinglePopulationBasedAlgorithm<Entity> currentAlgorithm : ca) {
            Fitness best = null;
            for (Entity e : currentAlgorithm.getTopology()) {
                if (best == null || ((Fitness) e.get(Property.BEST_FITNESS)).compareTo(best) > 0) {
                    best = ((Fitness) e.get(Property.BEST_FITNESS));
                }
            }
            fitness.add(best.getValue());
        }
        return fitness.build();
    }
}
