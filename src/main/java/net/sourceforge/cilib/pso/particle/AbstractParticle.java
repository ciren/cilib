/*
 * Particle.java
 *
 * Created on January 15, 2003, 8:27 PM
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
 *
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.particle.initialisation.PositionInitialisationStrategy;
import net.sourceforge.cilib.pso.particle.initialisation.RandomizedPositionInitialisationStrategy;
import net.sourceforge.cilib.pso.particle.initialisation.VelocityInitialisationStrategy;
import net.sourceforge.cilib.pso.particle.initialisation.ZeroInitialVelocityStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.MemoryNeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.StandardPositionUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public abstract class AbstractParticle extends AbstractEntity implements Particle {
	
    protected NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy;
    protected PositionUpdateStrategy positionUpdateStrategy;
    protected VelocityUpdateStrategy velocityUpdateStrategy;
    
    protected VelocityInitialisationStrategy velocityInitialisationStrategy;
    // TODO: Factor this out into a Particle intialisation strategy.... keep in mind the heterogeneous swarm thingy
    protected PositionInitialisationStrategy positionInitialisationStrategy;
    //    protected PersonalBestInitialisationStrategy personalBestInitialisationStrategy;
    
    private int id;

    /**
     * 
     *
     */
	public AbstractParticle() {
		neighbourhoodBestUpdateStrategy = new MemoryNeighbourhoodBestUpdateStrategy();
		positionUpdateStrategy = new StandardPositionUpdateStrategy();
		velocityUpdateStrategy = new StandardVelocityUpdate();
		
		positionInitialisationStrategy = new RandomizedPositionInitialisationStrategy();
		velocityInitialisationStrategy = new ZeroInitialVelocityStrategy();
	}

	/**
	 * 
	 * @return
	 */
    public abstract Particle clone();

	/**
	 * Returns the String representation of this Particle's ID
	 * @return the ID of this Particle as a String
	 */
	public String getId() {
		return String.valueOf(this.id);
	}

    /**
     * 
     */
    public void setId(String id) {
    	this.id = Integer.parseInt(id);
    }
    
    /**
     * 
     * @param id
     */
    public void setId(int id) {
    	this.id = id;
    }
    
    public void calculateFitness() {
    	calculateFitness(true);
    }
    
    public abstract void calculateFitness(boolean count);

    /**
     * Get the Fitness of the current Particle 
     * @return 
     */
    public abstract Fitness getFitness();
    
    /**
     * 
     * @return
     */
    public abstract Fitness getBestFitness();
    
    /**
     * 
     * @return
     */
    public abstract int getDimension();
   
    /**
     * Get the position of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s position.
     */
    public abstract Type getPosition();
    
    /**
     * Get the best position of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representng the <tt>Particle</tt>'s best position.
     */
    public abstract Type getBestPosition();
    
    
    /**
     * Get the velocity representation of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s velocity.
     */
    public abstract Type getVelocity();

    /**
     * Set the neighbourhood best particle for the current Particle based on the
     * topology of the current particle.
     * 
     * @param particle The particle to use as the current particle's neighhod best particle
     */
    public abstract void setNeighbourhoodBest(Particle particle);
    
    /**
     * Get the current <tt>Particle</tt>'s neighbourhood best.
     * @return The neighbourhood best of the <tt>Particle</tt>
     */
    public abstract Particle getNeighbourhoodBest();

    
    /**
     * Update the position of the <tt>Particle</tt>.
     */
    public abstract void updatePosition();
    
    /**
     * Update the velocity based on the provided <tt>VelocityUpdateStrategy</tt>.
     * @param vu The <tt>VelocityUpdateStrategy</tt> to use.
     */
    public abstract void updateVelocity();
    
    /**
     * Update the <tt>ControlParameters</tt> associated with the <tt>Particle</tt>.
     */
    public abstract void updateControlParameters();
    
    /**
     * Get the social best fitness for the particle based on the currently employed
     * <code>NeighbourhoodBestUpdateStrategy</code>
     * 
     * @see net.sourceforge.cilib.pso.positionupdatestrategies.NeighbourhoodBestUpdateStrategy#getSocialBest()
     * @return A <code>Fitness</code> object representing the best social fitness of
     *         the current strategy
	 */
	public Fitness getSocialBestFitness() {
		return neighbourhoodBestUpdateStrategy.getSocialBestFitness(this);
	}
	
	/**
	 * Get the reference to the currently employed <code>NeighbourhoodBestUpdateStrategy</code>
	 * 
	 * @see net.sourceforge.cilib.pso.particle#getNeighbourhoodBestUpdateStrategy()
	 * @return A reference to the current <code>NeighbourhoodBestUpdateStrategy</code> object
	 */
	public NeighbourhoodBestUpdateStrategy getNeighbourhoodBestUpdateStrategy() {
		return this.neighbourhoodBestUpdateStrategy;
	}

	/**
	 * Set the <code>NeighbourhoodBestUpdateStrategy</code> to be used by the <code>Particle</code>
	 * 
	 * @see net.sourceforge.cilib.pso.particle#setNeighbourhoodBestUpdateStrategy(net.sourceforge.cilib.pso.positionupdatestrategies.NeighbourhoodBestUpdateStrategy)
	 * @param neighbourhoodBestUpdateStrategy The <code>NeighbourhoodBestUpdateStrategy</code> to be used
	 */
	public void setNeighbourhoodBestUpdateStrategy(NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy) {
		this.neighbourhoodBestUpdateStrategy = neighbourhoodBestUpdateStrategy;
	}

	/**
	 * Get the current <tt>PostionUpdateStrategy</tt> associated with this <tt>Particle</tt>.
	 * @return The currently associated <tt>PositionUpdateStrategy</tt>.
	 */
	public PositionUpdateStrategy getPositionUpdateStrategy() {
		return positionUpdateStrategy;
	}
	
	
	/**
	 * Set the <tt>PostionUpdateStrategy</tt> for the <tt>Particle</tt>.
	 * @param positionUpdateStrategy The <tt>PositionUpdateStrategy</tt> to use.
	 */
	public void setPositionUpdateStrategy(
			PositionUpdateStrategy positionUpdateStrategy) {
		this.positionUpdateStrategy = positionUpdateStrategy;
	}
	
	
	
	/**
	 * Get the {@see net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy}
	 * of the current particle.
	 * 
	 * @return Returns the velocityUpdateStrategy.
	 */
	public VelocityUpdateStrategy getVelocityUpdateStrategy() {
		return velocityUpdateStrategy;
	}

	/**
	 * Set the velocity updating strategy for the particle.
	 * @param velocityUpdateStrategy The velocityUpdateStrategy to set.
	 */
	public void setVelocityUpdateStrategy(
			VelocityUpdateStrategy velocityUpdateStrategy) {
		this.velocityUpdateStrategy = velocityUpdateStrategy;
	}

	/**
	 * Get the {@see net.sourceforge.cilib.pso.particle.initialisation.VelocityInitialisationStrategy}
	 * @return 
	 */
	public VelocityInitialisationStrategy getVelocityInitialisationStrategy() {
		return velocityInitialisationStrategy;
	}

	/**
	 * Set the velocityInitialisationStrategy
	 * @param velocityInitialisationStrategy
	 */
	public void setVelocityInitialisationStrategy(
			VelocityInitialisationStrategy velocityInitialisationStrategy) {
		this.velocityInitialisationStrategy = velocityInitialisationStrategy;
	}
	
	public PositionInitialisationStrategy getPositionInitialisationStrategy() {
		return positionInitialisationStrategy;
	}

	public void setPositionInitialisationStrategy(
			PositionInitialisationStrategy positionInitialisationStrategy) {
		this.positionInitialisationStrategy = positionInitialisationStrategy;
	}

	public int compareTo(Entity o) {
		return getFitness().compareTo(o.getFitness());
	}

}
