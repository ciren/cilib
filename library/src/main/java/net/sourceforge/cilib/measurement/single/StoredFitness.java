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
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Real;

/**
 * This measurement only works for PSO. Simply find the Entity with the Best person best fitness in the population without re-calculating the fitness.
 */
public class StoredFitness implements Measurement<Real> {

    private static final long serialVersionUID = 6502384299554109943L;

    /**
     * {@inheritDoc}
     */
    @Override
    public StoredFitness getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Fitness best = null;
        PopulationBasedAlgorithm currentAlgorithm = (PopulationBasedAlgorithm) algorithm;
        for (Entity e : currentAlgorithm.getTopology()) {
            if (best == null || ((Fitness) e.getProperties().get(EntityType.Particle.BEST_FITNESS)).compareTo(best) > 0) {
                best = ((Fitness) e.getProperties().get(EntityType.Particle.BEST_FITNESS));
            }
        }
        return Real.valueOf(best.getValue());
    }
}
