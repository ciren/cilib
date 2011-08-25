/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.pso.pbestupdate.StandardPersonalBestUpdateStrategy;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.ConstantInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.StandardPBestPositionInitializationStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.pbestupdate.PersonalBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.MemoryNeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * This class defines the common behavior available for all {@linkplain Particle}
 * instances.
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public abstract class AbstractParticle extends AbstractEntity implements Particle {
    private static final long serialVersionUID = 7511192728112990230L;

    protected ParticleBehavior behavior;

    protected InitializationStrategy<Particle> velocityInitializationStrategy;
    protected InitializationStrategy<Particle> positionInitialisationStrategy;
    protected InitializationStrategy<Particle> personalBestInitialisationStrategy;
    
    protected PersonalBestUpdateStrategy personalBestUpdateStrategy;
    protected NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy;

    /**
     * Default constructor for all Particles.
     */
    public AbstractParticle() {
        this.behavior = new ParticleBehavior();

        this.velocityInitializationStrategy = new ConstantInitializationStrategy<Particle>(0.0);
        this.positionInitialisationStrategy = new RandomInitializationStrategy<Particle>();
        this.personalBestInitialisationStrategy = new StandardPBestPositionInitializationStrategy();

        this.personalBestUpdateStrategy = new StandardPersonalBestUpdateStrategy();
        this.neighbourhoodBestUpdateStrategy = new MemoryNeighbourhoodBestUpdateStrategy();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public AbstractParticle(AbstractParticle copy) {
        super(copy);
        this.behavior = copy.behavior.getClone();
        
        this.velocityInitializationStrategy = copy.velocityInitializationStrategy.getClone();
        this.positionInitialisationStrategy = copy.positionInitialisationStrategy.getClone();
        this.personalBestInitialisationStrategy = copy.personalBestInitialisationStrategy.getClone();

        this.personalBestUpdateStrategy = copy.personalBestUpdateStrategy.getClone();
        this.neighbourhoodBestUpdateStrategy = copy.neighbourhoodBestUpdateStrategy.getClone();
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
        return super.equals(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void calculateFitness();

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
    public abstract StructuredType getPosition();

    /**
     * Get the best position of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representng the <tt>Particle</tt>'s best position.
     */
    @Override
    public abstract StructuredType getBestPosition();

    /**
     * Get the velocity representation of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s velocity.
     */
    @Override
    public abstract StructuredType getVelocity();

    /**
     * Get the global guide of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representng the <tt>Particle</tt>'s global guide.
     */
    @Override
    public StructuredType getGlobalGuide() {
        return this.behavior.getGlobalGuideProvider().get(this);
    }

    /**
     * Get the local guide of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representng the <tt>Particle</tt>'s local guide.
     */
    @Override
    public StructuredType getLocalGuide() {
        return this.behavior.getLocalGuideProvider().get(this);
    }

    /**
     * Set the neighbourhood best particle for the current Particle based on the
     * topology of the current particle.
     *
     * @param particle The particle to use as the current particle's neighborhood best particle
     */
    @Override
    public abstract void setNeighbourhoodBest(Particle particle);

    /**
     * Get the current <tt>Particle</tt>'s neighborhood best.
     * @return The neighborhood best of the <tt>Particle</tt>
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
     * Get the current <tt>PositionProvider</tt> associated with this <tt>Particle</tt>.
     * @return The currently associated <tt>PositionProvider</tt>.
     */
    @Override
    public PositionProvider getPositionProvider() {
        return this.behavior.getPositionProvider();
    }

    /**
     * Set the <tt>PositionProvider</tt> for the <tt>Particle</tt>.
     * @param positionProvider The <tt>PositionProvider</tt> to use.
     */
    @Override
    public void setPositionProvider(PositionProvider positionProvider) {
        this.behavior.setPositionProvider(positionProvider);
    }

    /**
     * Get the {@see net.sourceforge.cilib.pso.velocityprovider.VelocityProvider}
     * of the current particle.
     *
     * @return Returns the VelocityProvider.
     */
    @Override
    public VelocityProvider getVelocityProvider() {
        return this.behavior.getVelocityProvider();
    }

    /**
     * Set the velocity updating strategy for the particle.
     * @param velocityProvider The VelocityProvider to set.
     */
    @Override
    public void setVelocityProvider(VelocityProvider velocityProvider) {
        this.behavior.setVelocityProvider(velocityProvider);
    }

    /**
     * Get the current global <tt>GuideProvider</tt> associated with this <tt>Particle</tt>.
     * @return The currently associated global <tt>GuideProvider</tt>.
     */
    public GuideProvider getGlobalGuideProvider() {
        return this.behavior.getGlobalGuideProvider();
    }

    /**
     * Set the <tt>GuideProvider</tt> for the <tt>Particle</tt>.
     * @param globalGuideProvider The global <tt>GuideProvider</tt> to use.
     */
    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.behavior.setGlobalGuideProvider(globalGuideProvider);
    }

    /**
     * Get the current local <tt>GuideProvider</tt> associated with this <tt>Particle</tt>.
     * @return The currently associated local <tt>GuideProvider</tt>.
     */
    public GuideProvider getLocalGuideProvider() {
        return this.behavior.getLocalGuideProvider();
    }

    /**
     * Set the <tt>GuideProvider</tt> for the <tt>Particle</tt>.
     * @param localGuideProvider The local <tt>GuideProvider</tt> to use.
     */
    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.behavior.setLocalGuideProvider(localGuideProvider);
    }

    /**
     * Get the {@link net.sourceforge.cilib.entity.initialization.InitializationStrategy}.
     * @return The current {@link net.sourceforge.cilib.entity.initialization.InitializationStrategy}.
     */
    @Override
    public InitializationStrategy getVelocityInitializationStrategy() {
        return this.velocityInitializationStrategy;
    }

    /**
     * Set the velocityInitializationStrategy.
     * @param initializationStrategy The value to set.
     */
    @Override
    public void setVelocityInitializationStrategy(InitializationStrategy initializationStrategy) {
        this.velocityInitializationStrategy = initializationStrategy;
    }

    /**
     * Get the current {@linkplain PositionInitialisationStrategy}.
     * @return The current {@linkplain PositionInitialisationStrategy}.
     */
    public InitializationStrategy<Particle> getPositionInitialisationStrategy() {
        return this.positionInitialisationStrategy;
    }

    /**
     * Set the {@linkplain PositionInitialisationStrategy} to be used.
     * @param positionInitialisationStrategy The value to set.
     */
    public void setPositionInitialisationStrategy(InitializationStrategy positionInitialisationStrategy) {
        this.positionInitialisationStrategy = positionInitialisationStrategy;
    }

    /**
     * Get the reference to the currently employed <code>NeighbourhoodBestUpdateStrategy</code>.
     * @return A reference to the current <code>NeighbourhoodBestUpdateStrategy</code> object
     */
    @Override
    public NeighbourhoodBestUpdateStrategy getNeighbourhoodBestUpdateStrategy() {
        return this.neighbourhoodBestUpdateStrategy;
    }

    /**
     * Set the <code>NeighbourhoodBestUpdateStrategy</code> to be used by the {@linkplain Entity}.
     * @param neighbourhoodBestUpdateStrategy The <code>NeighbourhoodBestUpdateStrategy</code> to be used
     */
    @Override
    public void setNeighbourhoodBestUpdateStrategy(NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy) {
        this.neighbourhoodBestUpdateStrategy = neighbourhoodBestUpdateStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getSocialFitness() {
        return this.neighbourhoodBestUpdateStrategy.getSocialBestFitness(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Entity o) {
        return this.getFitness().compareTo(o.getFitness());
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

    @Override
    public ParticleBehavior getParticleBehavior() {
        return this.behavior;
    }

    @Override
    public void setParticleBehavior(ParticleBehavior particleBehavior) {
        this.behavior = particleBehavior;
    }

    public void setPersonalBestInitialisationStrategy(InitializationStrategy<Particle> personalBestInitialisationStrategy) {
        this.personalBestInitialisationStrategy = personalBestInitialisationStrategy;
    }
}
