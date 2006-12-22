/*
 * GBestAbsorptionStrategy.java
 *
 * Created on 13 May 2006
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.pso.niching;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * 
 *@author 'Segun
 *
 */

public class GBestAbsorptionStrategy<E extends PopulationBasedAlgorithm> implements AbsorptionStrategy<E> {
    
    public void absorb(E mainSwarm, Collection<? extends E> subSwarms) {
        
        ListIterator mainSwarmIterator = mainSwarm.getTopology().listIterator();
        
        while(mainSwarmIterator.hasNext()) {
            Particle mainSwarmParticle = (Particle) mainSwarmIterator.next();
            
            Iterator subSwarmsIterator = subSwarms.iterator();
            
            while(subSwarmsIterator.hasNext()) {
                PSO subSwarm = (PSO)subSwarmsIterator.next();
                double subSwarmRadius = subSwarm.getRadius();
                
                Particle subSwarmBestParticle = subSwarm.getBestParticle();
                Vector subSwarmBestParticlePosition = (Vector)subSwarmBestParticle.getPosition();
                Vector mainSwarmParticlePosition = (Vector)mainSwarmParticle.getPosition();
                
                DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
                double distance = distanceMeasure.distance(subSwarmBestParticlePosition, mainSwarmParticlePosition);
                
                if(subSwarmRadius >= distance) {
                    subSwarm.getTopology().add(mainSwarmParticle);
                    mainSwarmIterator.remove();
                    break;
                    // mainSwarm.getTopology().getAll().remove(mainSwarmParticle); // remove the sub-swarm absorbed particle from the main swarm
                }
                
            }
            
        }
        
    }
    
}
