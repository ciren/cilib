/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.type.types.Int;

/**
 * <p>
 * Implementation of {@link GuideUpdateStrategy} where a particle's guide
 * can get updated if the new guide is not dominated by the current guide,
 * i.e. both of the guides are non-dominated. If both guides are non-
 * dominated the new guide is selected.
 * </p>
 * 
 * @author Marde Greeff
 *
 */
public class NonDominatedPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

   /**
    * {@inheritDoc}
    */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * If the current fitness is better than the best fitness, or both are
     * non-dominated, update the best fitness to the current fitness.
     *
     * If the current fitness is not updated, increase the particle's pbest stagnation counter.
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        Algorithm topLevelAlgorithm = AbstractAlgorithm.getAlgorithmList().get(0);
        Problem problem = topLevelAlgorithm.getOptimisationProblem();

        if (particle.getFitness().getClass().getName().matches("MinimisationFitness")) {
         if ((particle.getBestFitness() == null) || (problem.getFitness(particle.getPosition()).compareTo(problem.getFitness(particle.getBestPosition())) >= 0)) {
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
            return;
         }
        }
         else if (particle.getFitness().getClass().getName().matches("StandardMOFitness")) {
             if ((((MOFitness)particle.getBestFitness()) == null) || (((MOFitness)problem.getFitness(particle.getPosition())).compareTo(((MOFitness)problem.getFitness(particle.getBestPosition()))) >= 0)) {
                particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
                return;
            }
         }
        
        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }

}
