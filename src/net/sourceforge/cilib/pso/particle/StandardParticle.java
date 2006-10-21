/*
 * StandardParticle.java
 *
 * Created on September 22, 2003, 1:29 PM
 *
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

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class StandardParticle extends Particle {
    
    protected Vector position;
    protected Vector bestPosition;
    protected Vector velocity;

    private Fitness fitness;
    private Fitness bestFitness;

    private Particle neighbourhoodBest;
    
    
    /** Creates a new instance of StandardParticle */
    public StandardParticle() {
    	super();
        position = new MixedVector();
        bestPosition = new MixedVector();
        velocity = new MixedVector();
    }
    
    
    /**
     * Copy Constructor.
     * @param copy
     */
    public StandardParticle(StandardParticle copy) {
    	this.velocityUpdateStrategy = copy.velocityUpdateStrategy.clone(); // Check this
    	this.positionUpdateStrategy = copy.positionUpdateStrategy.clone();
    	this.neighbourhoodBestUpdateStrategy = copy.neighbourhoodBestUpdateStrategy;
    	this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.clone();
    	    	
    	this.position = copy.position.clone();
    	this.bestPosition = copy.position.clone();
    	this.velocity = copy.position.clone();
    }
    
    /**
     * 
     */
    public StandardParticle clone() {
       	return new StandardParticle(this);
    }
    
    public Fitness getBestFitness() {
        return bestFitness;
    }
    
    public Vector getBestPosition() {
        return bestPosition;
    }
    
    public void setBestPosition(Type bestPosition) {
    	this.bestPosition = (Vector) bestPosition;
    }
    
    public int getDimension() {
    	return position.getDimension();
    }
    
    public Fitness getFitness() {
        return fitness;
    }
    
    public Particle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public Vector getVelocity() {
        return velocity;
    }
    
    
    public void initialise(OptimisationProblem problem) {
        setId(PSO.getNextParticleId());
        
       	position = (Vector) problem.getDomain().getBuiltRepresenation().clone();
		position.randomise();

		// Make a deep-copy of the best position
		bestPosition = position.clone();
        
        // Create the velocity vector by cloning the position and setting all the values
        // within the velocity to 0
        velocity = position.clone();
        velocityInitialisationStrategy.initialise(this);
        
        fitness = InferiorFitness.instance();
        bestFitness = InferiorFitness.instance();
        neighbourhoodBest = this;
    }
    
    
    /**
     * 
     */
    public void updatePosition() {
    	this.positionUpdateStrategy.updatePosition(this);
    }
    

    /**
     * 
     */
    public void setFitness(Fitness fitness) {
        this.fitness = fitness;
        if (fitness.compareTo(bestFitness) > 0) {
            bestFitness = fitness;
     
            for (int i = 0; i < position.getDimension(); ++i) {
            	bestPosition.set(i, position.get(i));
            }
        }
    }
    
    
    /**
     * 
     */
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = particle;
    }
    
    
    /**
     * 
     */
    public void updateVelocity() {
    	this.velocityUpdateStrategy.updateVelocity(this);
    }
    
    
    /**
     * 
     */
/*    public String getId() {
        return String.valueOf(id);
    }
    
    
    public void setId(String id) {
    	this.id = Integer.valueOf(id);
    }*/
    
    /**
     * 
     */
    public Particle getDecorator(Class decorator) {
        throw new RuntimeException("This is not a decorator");
    }

	public Type get() {
		return getPosition();
	}
	
	public void set(Type type) {
		this.position = (Vector) type;
	}

	public void setDimension(int dim) {
		// TODO Auto-generated method stub
	}

	
	public Type getBehaviouralParameters() {
		return null;
	}

	public void setBehaviouralParameters(Type type) {
			
	}


	// Reinitialise all the things based on the defined initialisation strategy
	public void reinitialise() {
		this.velocityInitialisationStrategy.initialise(this);
	}
}
