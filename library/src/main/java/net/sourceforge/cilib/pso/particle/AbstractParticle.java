/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.initialisation.ConstantInitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.RandomInitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.StandardPBestPositionInitialisationStrategy;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.pbestupdate.PersonalBestUpdateStrategy;
import net.sourceforge.cilib.pso.pbestupdate.StandardPersonalBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.MemoryNeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * This class defines the common behavior available for all {@linkplain Particle}
 * instances.
 *
 */
public abstract class AbstractParticle extends AbstractEntity implements Particle {
    private static final long serialVersionUID = 7511192728112990230L;

    protected ParticleBehavior behavior;

    protected InitialisationStrategy<Particle> velocityInitialisationStrategy;
    protected InitialisationStrategy<Particle> positionInitialisationStrategy;
    protected InitialisationStrategy<Particle> personalBestInitialisationStrategy;

    protected PersonalBestUpdateStrategy personalBestUpdateStrategy;
    protected NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy;

    /**
     * Default constructor for all Particles.
     */
    public AbstractParticle() {
        this.behavior = new ParticleBehavior();

        this.velocityInitialisationStrategy = new ConstantInitialisationStrategy<Particle>(0.0);
        this.positionInitialisationStrategy = new RandomInitialisationStrategy<Particle>();
        this.personalBestInitialisationStrategy = new StandardPBestPositionInitialisationStrategy();

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

        this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.getClone();
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
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s best position.
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
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s global guide.
     */
    @Override
    public StructuredType getGlobalGuide() {
        return this.behavior.getGlobalGuideProvider().get(this);
    }

    /**
     * Get the local guide of the <tt>Particle</tt>.
     * @return A <tt>Type</tt> representing the <tt>Particle</tt>'s local guide.
     */
    @Override
    public StructuredType getLocalGuide() {
        return this.behavior.getLocalGuideProvider().get(this);
    }

    /**
     * Set the neighbourhood best particle for the current Particle based on the
     * topology of the current particle.
     *
     * @param particle The particle to use as the current particle's neighbourhood best particle
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
     * Get the {@link net.sourceforge.cilib.entity.initialisation.InitialisationStrategy}.
     * @return The current {@link net.sourceforge.cilib.entity.initialisation.InitialisationStrategy}.
     */
    @Override
    public InitialisationStrategy getVelocityInitialisationStrategy() {
        return this.velocityInitialisationStrategy;
    }

    /**
     * Set the velocityInitialisationStrategy.
     * @param initialisationStrategy The value to set.
     */
    @Override
    public void setVelocityInitialisationStrategy(InitialisationStrategy initialisationStrategy) {
        this.velocityInitialisationStrategy = initialisationStrategy;
    }

    /**
     * Get the current {@linkplain PositionInitialisationStrategy}.
     * @return The current {@linkplain PositionInitialisationStrategy}.
     */
    public InitialisationStrategy<Particle> getPositionInitialisationStrategy() {
        return this.positionInitialisationStrategy;
    }

    /**
     * Set the {@linkplain PositionInitialisationStrategy} to be used.
     * @param positionInitialisationStrategy The value to set.
     */
    public void setPositionInitialisationStrategy(InitialisationStrategy positionInitialisationStrategy) {
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

    public void setPersonalBestInitialisationStrategy(InitialisationStrategy<Particle> personalBestInitialisationStrategy) {
        this.personalBestInitialisationStrategy = personalBestInitialisationStrategy;
    }
}
