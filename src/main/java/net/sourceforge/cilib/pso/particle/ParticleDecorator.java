/*
 * ParticleDecorator.java
 *
 * Created on November 11, 2003, 1:00 PM
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
 */

package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 * @author  Edwin Peer
 */
public abstract class ParticleDecorator extends Particle {
	
	private Particle target;
	    
	public ParticleDecorator() {
		this.neighbourhoodBestUpdateStrategy = null;
		this.positionUpdateStrategy = null;
		this.velocityUpdateStrategy = null;
		
		target = null;
	}
	
    public ParticleDecorator(Particle target) {
        this.target = target;
    }
    
    public void setTarget(Particle target) {
    	this.target = target;
    }
    
    public Particle getTarget() {
    	return target;
    }
    
    public abstract ParticleDecorator clone();
    
    public Fitness getBestFitness() {
        return target.getBestFitness();
    }
    
    public Type getBestPosition() {
        return target.getBestPosition();
    }
    
    /*public void setBestPosition(Type bestPosition) {
    	this.target.setBestPosition(bestPosition);
    }*/
    
    public Particle getDecorator(Class decorator) {
        if (this.getClass().equals(decorator)) {
            return this;
        }
        else {
            return target.getDecorator(decorator);
        }
    }

    public int getDimension() {
        return target.getDimension();
    }
    
    public Fitness getFitness() {
        return target.getFitness();
    }
    
    public String getId() {
        return target.getId();
    }
    
    public void setId(String id) { 
    	this.target.setId(id);
    }
    
    public Particle getNeighbourhoodBest() {
        return target.getNeighbourhoodBest();
    }
    
    public Type getPosition() {
        return target.getPosition();
    }
    
    public Type getVelocity() {
        return target.getVelocity();
    }
    
    
    public void initialise(OptimisationProblem problem) {
        target.initialise(problem);
    }
    
    public void updatePosition() {
        target.updatePosition();
    }
    
    @Override
	public void calculateFitness() {
		target.calculateFitness();		
	}

	public void setNeighbourhoodBest(Particle particle) {
        target.setNeighbourhoodBest(particle);
    }
    
    
    public void updateVelocity() {
    	target.updateVelocity();
    }
    
    public VelocityUpdateStrategy getVelocityUpdateStrategy() {
    	return target.velocityUpdateStrategy;
    }
    
    public void setVelocityUpdateStrategy(VelocityUpdateStrategy velocityUpdateStrategy) {
    	target.setVelocityUpdateStrategy(velocityUpdateStrategy);
    }
    
        
    public Type get() {
    	return target.get();
    }
    
    public void set(Type type) {
    	this.target.set(type);
    }
    
    public int compareTo(Entity o) {
		// TODO Auto-generated method stub
		return 0;
	}
    
    public void setDimension(int dim) {
    	
    }
    
    public Type getBehaviouralParameters() {
    	return null;
    }
    
    public void setBehaviouralParameters(Type type) {
    	
    }
    
    public void reinitialise() {
    	
    }
}
