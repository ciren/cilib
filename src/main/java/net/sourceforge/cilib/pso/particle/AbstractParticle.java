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

import net.sourceforge.cilib.pso.positionupdatestrategies.StandardPersonalBestUpdateStrategy;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.particle.initialisation.PositionInitialisationStrategy;
import net.sourceforge.cilib.pso.particle.initialisation.RandomizedPositionInitialisationStrategy;
import net.sourceforge.cilib.pso.particle.initialisation.VelocityInitialisationStrategy;
import net.sourceforge.cilib.pso.particle.initialisation.ZeroInitialVelocityStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.MemoryNeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.PersonalBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.StandardPositionUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.Type;

/**
 * This class defines the common behaviour available for all {@linkplain Particle}
 * instances.
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public abstract class AbstractParticle extends AbstractEntity implements Particle {
    private static final long serialVersionUID = 7511192728112990230L;

    protected PositionUpdateStrategy positionUpdateStrategy;
    protected VelocityUpdateStrategy velocityUpdateStrategy;
    protected VelocityInitialisationStrategy velocityInitialisationStrategy;
    // TODO: Factor this out into a Particle intialisation strategy.... keep in mind the heterogeneous swarm thingy
    protected PositionInitialisationStrategy positionInitialisationStrategy;
    // protected PersonalBestInitialisationStrategy personalBestInitialisationStrategy;
    protected PersonalBestUpdateStrategy personalBestUpdateStrategy;

    private int id;

    /**
     * Default constructor for all Particles.
     */
    public AbstractParticle() {
        super();

        neighbourhoodBestUpdateStrategy = new MemoryNeighbourhoodBestUpdateStrategy();
        positionUpdateStrategy = new StandardPositionUpdateStrategy();
        velocityUpdateStrategy = new StandardVelocityUpdate();

        positionInitialisationStrategy = new RandomizedPositionInitialisationStrategy();
        velocityInitialisationStrategy = new ZeroInitialVelocityStrategy();

        personalBestUpdateStrategy = new StandardPersonalBestUpdateStrategy();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public AbstractParticle(AbstractParticle copy) {
        super(copy);
        this.neighbourhoodBestUpdateStrategy = copy.neighbourhoodBestUpdateStrategy.getClone();
        this.positionUpdateStrategy = copy.getPositionUpdateStrategy().getClone();
        this.velocityUpdateStrategy = copy.velocityUpdateStrategy.getClone();
        this.positionInitialisationStrategy = copy.positionInitialisationStrategy.getClone();
        this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.getClone();
        this.personalBestUpdateStrategy = copy.personalBestUpdateStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Particle getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        AbstractParticle other = (AbstractParticle) object;
        return  super.equals(other) &&
            (this.id == other.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + (Integer.valueOf(id).hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateFitness() {
        calculateFitness(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void calculateFitness(boolean count);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Fitness getBestFitness();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int getDimension();

    /**
     * Get the position of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s position.
     */
    @Override
    public abstract Type getPosition();

    /**
     * Get the best position of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representng the <tt>Particle</tt>'s best position.
     */
    @Override
    public abstract Type getBestPosition();

    /**
     * Get the velocity representation of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s velocity.
     */
    @Override
    public abstract Type getVelocity();

    /**
     * Set the neighbourhood best particle for the current Particle based on the
     * topology of the current particle.
     *
     * @param particle The particle to use as the current particle's neighhod best particle
     */
    @Override
    public abstract void setNeighbourhoodBest(Particle particle);

    /**
     * Get the current <tt>Particle</tt>'s neighbourhood best.
     * @return The neighbourhood best of the <tt>Particle</tt>
     */
    @Override
    public abstract Particle getNeighbourhoodBest();

    /**
     * Update the position of the <tt>Particle</tt>.
     */
    @Override
    public abstract void updatePosition();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void updateVelocity();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void updateControlParameters();

    /**
     * Get the current <tt>PostionUpdateStrategy</tt> associated with this <tt>Particle</tt>.
     * @return The currently associated <tt>PositionUpdateStrategy</tt>.
     */
    @Override
    public PositionUpdateStrategy getPositionUpdateStrategy() {
        return positionUpdateStrategy;
    }

    /**
     * Set the <tt>PostionUpdateStrategy</tt> for the <tt>Particle</tt>.
     * @param positionUpdateStrategy The <tt>PositionUpdateStrategy</tt> to use.
     */
    @Override
    public void setPositionUpdateStrategy(PositionUpdateStrategy positionUpdateStrategy) {
        this.positionUpdateStrategy = positionUpdateStrategy;
    }

    /**
     * Get the {@see net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy}
     * of the current particle.
     *
     * @return Returns the velocityUpdateStrategy.
     */
    @Override
    public VelocityUpdateStrategy getVelocityUpdateStrategy() {
        return velocityUpdateStrategy;
    }

    /**
     * Set the velocity updating strategy for the particle.
     * @param velocityUpdateStrategy The velocityUpdateStrategy to set.
     */
    @Override
    public void setVelocityUpdateStrategy(VelocityUpdateStrategy velocityUpdateStrategy) {
        this.velocityUpdateStrategy = velocityUpdateStrategy;
    }

    /**
     * Get the {@linkplain VelocityInitialisationStrategy}.
     * @return The current {@linkplain VelocityInitialisationStrategy}.
     */
    @Override
    public VelocityInitialisationStrategy getVelocityInitialisationStrategy() {
        return velocityInitialisationStrategy;
    }

    /**
     * Set the velocityInitialisationStrategy.
     * @param velocityInitialisationStrategy The value to set.
     */
    @Override
    public void setVelocityInitialisationStrategy(
            VelocityInitialisationStrategy velocityInitialisationStrategy) {
        this.velocityInitialisationStrategy = velocityInitialisationStrategy;
    }

    /**
     * Get the current {@linkplain PositionInitialisationStrategy}.
     * @return The current {@linkplain PositionInitialisationStrategy}.
     */
    public PositionInitialisationStrategy getPositionInitialisationStrategy() {
        return positionInitialisationStrategy;
    }

    /**
     * Set the {@linkplain PositionInitialisationStrategy} to be used.
     * @param positionInitialisationStrategy The value to set.
     */
    public void setPositionInitialisationStrategy(PositionInitialisationStrategy positionInitialisationStrategy) {
        this.positionInitialisationStrategy = positionInitialisationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Entity o) {
        return getFitness().compareTo(o.getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getPersonalBestUpdateStrategy() {
        return this.personalBestUpdateStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPersonalBestUpdateStrategy(PersonalBestUpdateStrategy personalBestUpdateStrategy) {
        this.personalBestUpdateStrategy = personalBestUpdateStrategy;
    }

}
