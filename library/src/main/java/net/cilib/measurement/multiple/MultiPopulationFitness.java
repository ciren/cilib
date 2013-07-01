/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.multiple;

import net.cilib.algorithm.Algorithm;
import net.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.entity.Entity;
import net.cilib.entity.EntityType;
import net.cilib.measurement.Measurement;
import net.cilib.problem.solution.Fitness;
import net.cilib.type.types.container.Vector;

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
                if (best == null || ((Fitness) e.getProperties().get(EntityType.Particle.BEST_FITNESS)).compareTo(best) > 0) {
                    best = ((Fitness) e.getProperties().get(EntityType.Particle.BEST_FITNESS));
                }
            }
            fitness.add(best.getValue());
        }
        return fitness.build();
    }
}
