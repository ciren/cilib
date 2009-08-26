/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.score.EntityScoreboard;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

/**
 * This class re-calculates all the personal best positions in the population. It will only work for PSO algorithms.
 * @author leo
 *
 */
public class CompetitiveCoevolutionParticleReevaluationResponseStrategy<E extends PopulationBasedAlgorithm>
        extends EnvironmentChangeResponseStrategy<E> {
    private static final long serialVersionUID = 2858321511836578500L;
    public CompetitiveCoevolutionParticleReevaluationResponseStrategy() {

    }

    public CompetitiveCoevolutionParticleReevaluationResponseStrategy(CompetitiveCoevolutionParticleReevaluationResponseStrategy<E> other) {

    }

    /**
     * {@inheritDoc}
     */
    public CompetitiveCoevolutionParticleReevaluationResponseStrategy<E> getClone() {
        return new CompetitiveCoevolutionParticleReevaluationResponseStrategy<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    public void performReaction(PopulationBasedAlgorithm algorithm) {
          //select new competitors and re-evaluate PBest vector of Particle
         PopulationBasedAlgorithm currentAlgorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.get(); //the current sub population algorithm
         int populationID = -1;
         for(Entity e: currentAlgorithm.getTopology().asList()) {
             if(!(e instanceof AbstractParticle))
                 throw new RuntimeException("CompetitiveCoevolutionParticleReevaluationResponseStrategy should only be used with Particles");
             if(populationID == -1)
                 populationID = ((Int)e.getProperties().get(EntityType.Coevolution.POPULATION_ID)).getInt();
             Blackboard<Enum<?>, Type> blackboard = new Blackboard<Enum<?>, Type>();
             blackboard.put(EntityType.CANDIDATE_SOLUTION, ((AbstractParticle)e).getBestPosition());
             blackboard.put(EntityType.Coevolution.BOARD, new EntityScoreboard());
             Fitness val = currentAlgorithm.getOptimisationProblem().getFitness(blackboard);
             e.getProperties().put(EntityType.Particle.BEST_FITNESS, val);
             //if currentV is better than re-evaluated pBest, then replace it
             if (e.getFitness().compareTo(e.getBestFitness()) > 0) {
                 e.getProperties().put(EntityType.Particle.BEST_FITNESS, e.getFitness());
                     e.getProperties().put(EntityType.Particle.BEST_POSITION, e.getProperties().get(EntityType.CANDIDATE_SOLUTION).getClone());
                 }
        }
    }

}
