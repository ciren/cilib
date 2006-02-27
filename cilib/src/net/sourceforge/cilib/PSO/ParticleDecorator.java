/*
 * ParticleDecorator.java
 *
 * Created on November 11, 2003, 1:00 PM
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.PSO;

import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;

/**
 *
 * @author  espeer
 */
public abstract class ParticleDecorator implements Particle, Cloneable {
    
	public ParticleDecorator() {
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
    
    public Object clone() throws CloneNotSupportedException {
        ParticleDecorator clone = (ParticleDecorator) super.clone();
        clone.target = (Particle) target.clone();
        
        return clone;
    }
    
    public Fitness getBestFitness() {
        return target.getBestFitness();
    }
    
    public double[] getBestPosition() {
        return target.getBestPosition();
    }
    
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
    
    public int getId() {
        return target.getId();
    }
    
    public Particle getNeighbourhoodBest() {
        return target.getNeighbourhoodBest();
    }
    
    public double[] getPosition() {
        return target.getPosition();
    }
    
    public double[] getVelocity() {
        return target.getVelocity();
    }
    
    public void initialise(OptimisationProblem problem, Initialiser i) {
        target.initialise(problem, i);
    }
    
    public void move() {
        target.move();
    }
    
    public void setFitness(Fitness fitness) {
        target.setFitness(fitness);
    }
    
    public void setNeighbourhoodBest(Particle particle) {
        target.setNeighbourhoodBest(particle);
    }
    
    public void updateVelocity(VelocityUpdate vu) {
        target.updateVelocity(vu);
    }
    
    private Particle target;
}
