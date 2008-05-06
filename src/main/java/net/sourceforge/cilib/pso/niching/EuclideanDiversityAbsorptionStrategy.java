/*
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
import net.sourceforge.cilib.controlparameter.ControlParameter;
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
 * @author Edrich
 * @param <E> Any type that extends {@linkplain PopulationBasedAlgorithm}.
 */
public class EuclideanDiversityAbsorptionStrategy<E extends PopulationBasedAlgorithm> implements AbsorptionStrategy<E>
{
    private ControlParameter threshold;

    /**
     * Create an instance of the absorption strategy, initialised with default values.
     */
    public EuclideanDiversityAbsorptionStrategy() {
    	this.threshold = new ConstantControlParameter(0.001);
    }

    /**
     * {@inheritDoc}
     */
    public void absorb(E mainSwarm, List<PopulationBasedAlgorithm> subSwarms) {
    	ListIterator<? extends Entity> mainSwarmIterator = mainSwarm.getTopology().listIterator();
    	
    	while (mainSwarmIterator.hasNext()) {
    		Particle mainSwarmParticle = (Particle) mainSwarmIterator.next();

    		Iterator<PopulationBasedAlgorithm> subSwarmsIterator = subSwarms.iterator();
    		while (subSwarmsIterator.hasNext()) {
    			PSO subSwarm = (PSO) subSwarmsIterator.next();
		
    			RadiusVisitor radiusVisitor = new RadiusVisitor();
    			subSwarm.accept(radiusVisitor);
    			double subSwarmRadius = radiusVisitor.getResult();

    			Particle subSwarmBestParticle = subSwarm.getTopology().getBestEntity();
    			Vector subSwarmBestParticlePosition = (Vector) subSwarmBestParticle.getPosition();
    			Vector mainSwarmParticlePosition = (Vector) mainSwarmParticle.getPosition();

    			DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
    			double distance = distanceMeasure.distance(subSwarmBestParticlePosition, mainSwarmParticlePosition);

    			if (distance < subSwarmRadius && checkEuclideanDiversityAroundGBest(mainSwarmParticle, subSwarm)) {
    				mainSwarmIterator.remove();

    				subSwarm.getTopology().add(mainSwarmParticle);

    				RandomizingControlParameter socialAcceleration = new RandomizingControlParameter();
    				socialAcceleration.setControlParameter(new ConstantControlParameter(1.2));
    				((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setSocialAcceleration(socialAcceleration);
		    
    				mainSwarm.getInitialisationStrategy().setEntityNumber(mainSwarm.getTopology().size());
    				subSwarm.getInitialisationStrategy().setEntityNumber(subSwarm.getTopology().size());
    				// System.out.println("absorbed - D:" + distance + " , R: " + subSwarmRadius + " , PID: " + mainSwarmParticle.getId());
    				break;
    			}

    		}

    	}

    }

    /**
     * Check the diversity around the GBest particle.
     * @param mainSwarm The main swarm {@linkplain Particle}.
     * @param subSwarm The subswarm to inspect.
     * @return the result of the the comparison.
     */
    private boolean checkEuclideanDiversityAroundGBest(Particle mainSwarm, PSO subSwarm) {
    	Vector center = (Vector) subSwarm.getTopology().getBestEntity().getPosition();
    	DistanceMeasure distance = new EuclideanDistanceMeasure();
    	double diameter = 0;
    	int count = 0;

    	for (Iterator<Particle> i = subSwarm.getTopology().iterator(); i.hasNext(); ++count) {
    		Entity other = i.next();
    		diameter += distance.distance(center, (Vector) other.getContents());
    	}

    	if ((diameter / (double) count) <= this.threshold.getParameter())
    		return true;

    	return false;
    }

    /**
     * Get the current threshold value.
     * @return The {@linkplain ControlParameter} representing the threshold value.
     */
    public ControlParameter getThreshold() {
    	return threshold;
    }

    /**
     * Set the threshold value.
     * @param threshold The {@linkplain ControlParameter} to set.
     */
    public void setThreshold(ControlParameter threshold) {
    	this.threshold = threshold;
    }

}
