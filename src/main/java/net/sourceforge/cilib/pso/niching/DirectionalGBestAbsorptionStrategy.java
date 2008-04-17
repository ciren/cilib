/*
 * DirectionalGBestAbsorptionStrategy.java
 *
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso.niching;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * 
 *@author Edrich
 *@param <E> The {@linkplain PopulationBasedAlgorithm} type.
 */
public class DirectionalGBestAbsorptionStrategy<E extends PopulationBasedAlgorithm> implements AbsorptionStrategy<E> {
    
    public void absorb(E mainSwarm, List<PopulationBasedAlgorithm> subSwarms) {
        
        ListIterator<? extends Entity> mainSwarmIterator = mainSwarm.getTopology().listIterator();
        while(mainSwarmIterator.hasNext()) {
            Particle mainSwarmParticle = (Particle) mainSwarmIterator.next();
            
            Iterator<PopulationBasedAlgorithm> subSwarmsIterator = subSwarms.iterator();    
            while(subSwarmsIterator.hasNext()) {            	
                PSO subSwarm = (PSO) subSwarmsIterator.next();
                
                RadiusVisitor radiusVisitor = new RadiusVisitor();
                subSwarm.accept(radiusVisitor);
                double subSwarmRadius = radiusVisitor.getResult();
                
                Particle subSwarmBestParticle = subSwarm.getTopology().getBestEntity();
                Vector subSwarmBestParticlePosition = (Vector) subSwarmBestParticle.getPosition();
                Vector mainSwarmParticlePosition = (Vector) mainSwarmParticle.getPosition();
                
                DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
                double distance = distanceMeasure.distance(subSwarmBestParticlePosition, mainSwarmParticlePosition);
                                
                if (distance <= subSwarmRadius && calculateDotProduct(calculateDirectionalVector(subSwarmBestParticle), calculateDirectionalVector(mainSwarmParticle)) < 0.0) {                   
                    subSwarm.getTopology().add(mainSwarmParticle);
                    
                    RandomizingControlParameter socialAcceleration = new RandomizingControlParameter();
                    socialAcceleration.setControlParameter(new ConstantControlParameter(1.2));
                    ((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setSocialAcceleration(socialAcceleration);
                                        
                    mainSwarmIterator.remove();
                    //System.out.println("absorbed - D:" + distance + " , R: " + subSwarmRadius + " , PID: " + mainSwarmParticle.getId());
                    break;
                }
                
            }
            
        }
        
    }
    
    private Vector calculateDirectionalVector(Particle a) {
	 Vector newPosition = (Vector) a.getPosition().getClone();
	Vector velocity =  (Vector) a.getVelocity().getClone();

	for (int i = 0; i < newPosition.getDimension(); i++) {
	    double value = newPosition.getReal(i);
	    value += velocity.getReal(i);
	    newPosition.setReal(i, value);
	}

	Vector direction = new Vector();
	direction = newPosition.subtract((Vector) a.getPosition());
	
	return direction;
    }

    private double calculateDotProduct(Vector a, Vector b) {
	return a.dot(b);
    }
    
}
