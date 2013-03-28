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
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.type.types.Int;

/**
 * Implementation of {@link PersonalBestUpdateStrategy} where a
 * {@link Particle}'s guide can get updated if the new guide is not dominated
 * by the current guide, i.e. both of the guides are non-dominated.
 * If both guides are non-dominated the new guide is selected.
 */
public class RelaxedNonDominatedPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

   /**
    * {@inheritDoc}
    */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * Updates the guide. If the new guide dominates the old guide, the new guide is
     * selected. However, if both guides are non-dominated, one of the guides is
     * randomly selected.
     * @param particle The particle who's guide is to be updated.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        Algorithm topLevelAlgorithm = AbstractAlgorithm.getAlgorithmList().get(0);
        Problem problem = topLevelAlgorithm.getOptimisationProblem();

        if (particle.getFitness().getClass().getName().matches("MinimisationFitness")) {
         if ((particle.getBestFitness() == null) || (problem.getFitness(particle.getPosition()).compareTo(problem.getFitness(particle.getBestPosition())) > 0)) {
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
            return;
         }
         else if ((particle.getBestFitness() == null) || (problem.getFitness(particle.getPosition()).compareTo(problem.getFitness(particle.getBestPosition())) == 0)) {
            int random = Rand.nextInt(2);
            if (random == 1) {
                particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
                return;
            }
         }
        }
         else if (particle.getFitness().getClass().getName().matches("StandardMOFitness")) {
             if ((((MOFitness)particle.getBestFitness()) == null) || (((MOFitness)problem.getFitness(particle.getPosition())).compareTo(((MOFitness)problem.getFitness(particle.getBestPosition()))) > 0)) {
                particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
                return;
            }
            else if ((((MOFitness)particle.getBestFitness()) == null) || (((MOFitness)problem.getFitness(particle.getPosition())).compareTo(((MOFitness)problem.getFitness(particle.getBestPosition()))) == 0)) {
                int random = Rand.nextInt(20);
                if (random > 10) {
                    particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
                    particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
                    particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
                    return;
                }
            }
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }

}
