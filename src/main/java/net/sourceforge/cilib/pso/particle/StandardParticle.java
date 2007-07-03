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

import java.util.Map;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.VectorBasedFitnessCalculator;

/**
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class StandardParticle extends AbstractParticle {
    private static final long serialVersionUID = 2610843008637279845L;
    
    protected Fitness fitness;
    protected Fitness bestFitness;

    protected Particle neighbourhoodBest;
    protected FitnessCalculator fitnessCalculator;
    
    /** Creates a new instance of StandardParticle */
    public StandardParticle() {
    	super();
    	
    	properties.put("position", new MixedVector());
    	properties.put("bestPosition", new MixedVector());
    	properties.put("velocity", new MixedVector());
        
        fitnessCalculator = new VectorBasedFitnessCalculator();
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
    	
    	this.fitnessCalculator = copy.fitnessCalculator.clone();
    	    	
    	for (Map.Entry<String, Type> entry : copy.properties.entrySet()) {
    		String key = entry.getKey().toString();
    		this.properties.put(key, entry.getValue().clone());
    	}
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
    	return (Vector) this.properties.get("bestPosition");
    }
        
    public int getDimension() {
    	return getPosition().getDimension();
    }
    
    public Fitness getFitness() {
        return fitness;
    }
    
    public Particle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }
    
    public Vector getPosition() {
    	return (Vector) this.properties.get("position");
    }
    
    public Vector getVelocity() {
        return (Vector) this.properties.get("velocity");
    }
    
    
    public void initialise(OptimisationProblem problem) {
        setId(PSO.getNextParticleId());
        
       	this.properties.put("position", (Vector) problem.getDomain().getBuiltRepresenation().clone());
		getPosition().randomise();

		// Make a deep-copy of the best position
		this.properties.put("bestPosition", getPosition().clone());
        
        // Create the velocity vector by cloning the position and setting all the values
        // within the velocity to 0
        this.properties.put("velocity", getPosition().clone());
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
    public void calculateFitness(boolean count) {
    	this.fitness = fitnessCalculator.getFitness(getPosition(), count);
    	if (fitness.compareTo(bestFitness) > 0) {
    		this.bestFitness = fitness;
    		this.properties.put("bestPosition", getPosition().clone());
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
    public Particle getDecorator(Class decorator) {
        throw new RuntimeException("This is not a decorator");
    }

	public Type getContents() {
		return getPosition();
	}
	
	public void setContents(Type type) {
		this.properties.put("position", type);
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

	public FitnessCalculator getFitnessCalculator() {
		return fitnessCalculator;
	}

	public void setFitnessCalculator(FitnessCalculator fitnessCalculator) {
		this.fitnessCalculator = fitnessCalculator;
	}
}
