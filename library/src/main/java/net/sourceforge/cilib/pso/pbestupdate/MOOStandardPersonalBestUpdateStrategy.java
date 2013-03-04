/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.Int;

/**
 * Update the personal best of the particle, based on the standard PSO definition
 * of the process.
 *
 */
public class MOOStandardPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

    private static final long serialVersionUID = 266386833476786081L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * If the current fitness is better than the current best fitness, update
     * the best fitness of the particle to equal the current fitness and make
     * the personal best position a clone of the current particle position.
     *
     * If the current fitness is not better than the current best fitness,
     * increase the particle's pbest stagnation counter.
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {

        Algorithm topLevelAlgorithm = AbstractAlgorithm.getAlgorithmList().get(0);
        Problem problem = topLevelAlgorithm.getOptimisationProblem();

        if ((particle.getBestFitness() == null) || (problem.getFitness(particle.getPosition()).compareTo(problem.getFitness(particle.getBestPosition())) > 0)) {
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
            return;
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }
}
