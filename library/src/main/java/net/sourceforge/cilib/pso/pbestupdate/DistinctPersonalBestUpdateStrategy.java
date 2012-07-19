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

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Int;

public class DistinctPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {
    
    private DistinctPositionProvider positionProvider;
    
    public DistinctPersonalBestUpdateStrategy() {
        this.positionProvider = new MutatedDistinctPositionProvider();
    }
    
    public DistinctPersonalBestUpdateStrategy(DistinctPersonalBestUpdateStrategy copy) {
        this.positionProvider = copy.positionProvider.getClone();
    }

    @Override
    public DistinctPersonalBestUpdateStrategy getClone() {
        return new DistinctPersonalBestUpdateStrategy(this);
    }

    @Override
    public void updatePersonalBest(Particle particle) {
        if (particle.getFitness().compareTo(particle.getBestFitness()) > 0) {
            particle.getParticleBehavior().incrementSuccessCounter();
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            
            Particle temp = particle.getClone();
            temp.setCandidateSolution(positionProvider.f(particle));
            
            Fitness tempFitness = particle.getFitnessCalculator().getFitness(temp);
            
            if (tempFitness.compareTo(particle.getFitness()) > 0) {
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, tempFitness);
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, temp.getCandidateSolution());
            } else {
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getCandidateSolution());
                
                particle.getProperties().put(EntityType.FITNESS, tempFitness);
                particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, temp.getCandidateSolution());
            }
            
            return;
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }

    public DistinctPositionProvider getPositionProvider() {
        return positionProvider;
    }

    public void setPositionProvider(DistinctPositionProvider positionProvider) {
        this.positionProvider = positionProvider;
    }
}
