/**
 * Copyright (C) 2003 - 2009
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

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.PersonalBestUpdateStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Definition of a particle.
 */
public interface Particle extends Entity, SocialEntity {

    /**
     * {@inheritDoc}
     */
    @Override
    public Particle getClone();

    /**
     * Get the current position of the {@linkplain Particle}.
     * @return The {@linkplain Type} representing the position.
     */
    public StructuredType getPosition();

    /**
     * Get the best position of the {@linkplain Particle}.
     * @return The {@linkplain Type} representing the best position.
     */
    public StructuredType getBestPosition();

    /**
     * Get the current velocity of the {@linkplain Particle}.
     * @return The {@linkplain Type} representing the velocity.
     */
    public StructuredType getVelocity();

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
    public InitializationStrategy getVelocityInitializationStrategy();

    /**
     * Set the initialisation strategy for the {@linkplain Particle}s velocity.
     * @param velocityInitialisationStrategy The velocity initialisation strategy to set.
     */
    public void setVelocityInitializationStrategy(InitializationStrategy velocityInitialisationStrategy);

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

    /**
     * Get the current strategy to perform personal best updates.
     * @return The current {@link PersonalBestUpdateStrategy}.
     */
    public PersonalBestUpdateStrategy getPersonalBestUpdateStrategy();

    /**
     * Set the strategy to perform personal best updates.
     * @param personalBestUpdateStrategy The instance to set.
     */
    public void setPersonalBestUpdateStrategy(PersonalBestUpdateStrategy personalBestUpdateStrategy);

}
