/*
 * Particle.java
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
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.particle.initialisation.VelocityInitialisationStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.Type;

/**
 * 
 *
 */
public interface Particle extends Entity {
	
	/**
	 * {@inheritDoc}
	 */
	public Particle getClone();

	/**
	 * Get the current position of the {@linkplain Particle}.
	 * @return The {@linkplain Type} representing the position.
	 */
	public Type getPosition();
	
	/**
	 * Get the best position of the {@linkplain Particle}.
	 * @return The {@linkplain Type} representing the best position.
	 */
	public Type getBestPosition();
	
	/**
	 * Get the current velocity of the {@linkplain Particle}.
	 * @return The {@linkplain Type} representing the velocity.
	 */
	public Type getVelocity();
	
	/**
	 * Get the current best particle within the {@linkplain Particle}s neighbourhood.
	 * @return The neighbourhood best particle.
	 */
	public Particle getNeighbourhoodBest();
	
	/**
	 * Set the current neighbourhood best particle within the current neighbourhood.
	 * @param particle The particle to set as the neighbourhood best.
	 */
	public void setNeighbourhoodBest(Particle particle);
	
	/**
	 * {@inheritDoc}
	 */
	public int getDimension();
	
	/**
	 * {@inheritDoc}
	 */
	public Fitness getFitness();
	
	/**
	 * Get the best {@linkplain Fitness} for the particle.
	 * @return The particle's best {@linkplain Fitness}.
	 */
	public Fitness getBestFitness();

	/**
	 * Get the current social best fitness. This {@linkplain Fitness} value is dependent
	 * on the current {@linkplain NeighbourhoodBestUpdateStrategy}.
	 * @return The fitness based on the currently set {@linkplain NeighbourhoodBestUpdateStrategy}.
	 */
	public Fitness getSocialBestFitness();
	
	/**
	 * Update the current position of the {@linkplain Particle}.
	 */
	public void updatePosition();
	
	/**
	 * Update the velocity of the {@linkplain Particle}.
	 */
	public void updateVelocity();
	
	/**
	 * Update all the {@linkplain ControlParameter}s that are maintained within the
	 * {@linkplain Particle}.
	 */
	public void updateControlParameters();
	
	/**
	 * Get the strategy that will be used to update the velocity.
	 * @return The current {@linkplain VelocityUpdateStrategy}.
	 */
	public VelocityUpdateStrategy getVelocityUpdateStrategy();
	
	/**
	 * Set the {@linkplain VelocityUpdateStrategy} to be used during velocity updates.
	 * @param velocityUpdateStrategy the {@linkplain VelocityUpdateStrategy} to use.
	 */
	public void setVelocityUpdateStrategy(VelocityUpdateStrategy velocityUpdateStrategy);
	
	/**
	 * Get the strategy for the intialisation of the velocity.
	 * @return The {@linkplain VelocityInitialisationStrategy} that is currently set.
	 */
	public VelocityInitialisationStrategy getVelocityInitialisationStrategy();
	
	/**
	 * Set the initialisation strategy for the {@linkplain Particle}s velocity.
	 * @param velocityInitialisationStrategy The velocity initialisation strategy to set.
	 */
	public void setVelocityInitialisationStrategy(VelocityInitialisationStrategy velocityInitialisationStrategy);
	
	/**
	 * Get the current {@linkplain PositionUpdateStrategy} of the {@linkplain Particle}.
	 * @return The current {@linkplain PositionUpdateStrategy}.
	 */
	public PositionUpdateStrategy getPositionUpdateStrategy();
	
	/**
	 * Set the {@linkplain PositionUpdateStrategy} to use for particle position updates.
	 * @param positionUpdateStrategy The {@linkplain PositionUpdateStrategy} to set.
	 */
	public void setPositionUpdateStrategy(PositionUpdateStrategy positionUpdateStrategy);

	
	// I don't like this method / idea
	public String getId();
	
}
