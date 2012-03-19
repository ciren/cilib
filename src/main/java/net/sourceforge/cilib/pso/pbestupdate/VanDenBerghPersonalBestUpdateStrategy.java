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
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.Int;

/**
 * This class updates the personal best only if the Parameterized Particle's parameters
 * satisfy Van den Bergh's inequality. Note that this is only applicable to ParameterizedParticles,
 * any other particle will be updated using the standard updateStrategy.
 * 
 * Reference: F. van den Bergh and A. P. Engelbrecht. A study of particle swarm optimization
 * particle trajectories. Inf. Sci., 176(8):937{971, April 2006.
 * 
 * @author Kristina
 */
public class VanDenBerghPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public VanDenBerghPersonalBestUpdateStrategy getClone() {
        return this;
    }

    /*
     * If parameterized particle is not used, then the update is 
     * a standard one as the particle has no parameters to check.
     * 
     * @param particle The particle whose personal best is updated
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        if (particle.getFitness().compareTo(particle.getBestFitness()) > 0) {
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
            return;
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }
    
    /*
     * If a parameterized particle is used, the personal best
     * is only updated if the parameters satisfy Van der Bergh's 
     * inequality
     * 
     * @param particle The particle whose personal best is updated
     */
    @Override
    public void updateParametizedPersonalBest(ParameterizedParticle particle) {
        double inertia = particle.getInertia().getParameter();
        double socialAcceleration = particle.getSocialAcceleration().getParameter();
        double cognitiveAcceleration = particle.getCognitiveAcceleration().getParameter();
        double vmax = particle.getVmax().getParameter();
        
        double inequalityRight = (socialAcceleration + cognitiveAcceleration)/2 - 1;
        
        if(inertia > inequalityRight) {
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
            particle.setBestInertia(particle.getInertia());
            particle.setBestCognitiveAcceleration(particle.getCognitiveAcceleration());
            particle.setBestSocialAcceleration(particle.getSocialAcceleration());
            particle.setBestVmax(particle.getVmax());
        }
        
    }
}
