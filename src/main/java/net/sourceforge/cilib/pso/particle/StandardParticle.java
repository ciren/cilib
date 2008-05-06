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
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.VectorBasedFitnessCalculator;

/**
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class StandardParticle extends AbstractParticle {
    private static final long serialVersionUID = 2610843008637279845L;
    
    protected Particle neighbourhoodBest;
    protected FitnessCalculator fitnessCalculator;
    
    /** Creates a new instance of StandardParticle. */
    public StandardParticle() {
    	super();
    	
    	properties.put("bestPosition", new Vector());
    	properties.put("velocity", new Vector());
        
        fitnessCalculator = new VectorBasedFitnessCalculator();
    }
    
    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public StandardParticle(StandardParticle copy) {
    	super(copy);
    	this.velocityUpdateStrategy = copy.velocityUpdateStrategy.getClone(); // Check this
    	this.positionUpdateStrategy = copy.positionUpdateStrategy.getClone();
    	this.neighbourhoodBestUpdateStrategy = copy.neighbourhoodBestUpdateStrategy;
    	this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.getClone();
    	
    	this.fitnessCalculator = copy.fitnessCalculator.getClone();
    }
    
    /**
     * 
     */
    public StandardParticle getClone() {
       	return new StandardParticle(this);
    }
    
    public Fitness getBestFitness() {
        return (Fitness) this.properties.get("bestFitness");
    }
    
    public Vector getBestPosition() {
    	return (Vector) this.properties.get("bestPosition");
    }
        
    public int getDimension() {
    	return getPosition().getDimension();
    }
    
    public Particle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }
    
    public Vector getPosition() {
    	return (Vector) getContents();
    }
    
    public Vector getVelocity() {
        return (Vector) this.properties.get("velocity");
    }
    
    
    public void initialise(OptimisationProblem problem) {
        setId(PSO.getNextParticleId());
        
        getPositionInitialisationStrategy().initialise(this, problem);
        
        // Create the velocity vector by cloning the position and setting all the values
        // within the velocity to 0
        this.properties.put("velocity", getPosition().getClone());
        velocityInitialisationStrategy.initialise(this);
        
        this.properties.put("fitness", InferiorFitness.instance());
        this.properties.put("bestFitness", InferiorFitness.instance());
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
    	Fitness fitness = fitnessCalculator.getFitness(getPosition(), count);
    	this.properties.put("fitness", fitness);
    	if (fitness.compareTo(getBestFitness()) > 0) {
    		this.properties.put("bestFitness", fitness);
    		this.properties.put("bestPosition", getPosition().getClone());
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
    

    public void updateControlParameters() {
		this.velocityUpdateStrategy.updateControlParameters(this);
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
