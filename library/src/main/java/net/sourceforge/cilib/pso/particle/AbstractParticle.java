/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.initialisation.ConstantInitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.RandomInitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.StandardPBestPositionInitialisationStrategy;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.pbestupdate.PersonalBestUpdateStrategy;
import net.sourceforge.cilib.pso.pbestupdate.StandardPersonalBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.MemoryNeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * This class defines the common behavior available for all {@link Particle}
 * instances.
 */
public abstract class AbstractParticle extends AbstractEntity implements Particle {
    private static final long serialVersionUID = 7511192728112990230L;

    protected InitialisationStrategy<Particle> velocityInitialisationStrategy;
    protected InitialisationStrategy<Particle> positionInitialisationStrategy;
    protected InitialisationStrategy<Particle> personalBestInitialisationStrategy;

    protected PersonalBestUpdateStrategy personalBestUpdateStrategy;
    protected NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy;

    /**
     * Default constructor for all {@code Particle}s.
     */
    public AbstractParticle() {
        this.behaviour = new StandardParticleBehaviour();

        this.velocityInitialisationStrategy = new ConstantInitialisationStrategy<Particle>(0.0);
        this.positionInitialisationStrategy = new RandomInitialisationStrategy<Particle>();
        this.personalBestInitialisationStrategy = new StandardPBestPositionInitialisationStrategy();

        this.personalBestUpdateStrategy = new StandardPersonalBestUpdateStrategy();
        this.neighbourhoodBestUpdateStrategy = new MemoryNeighbourhoodBestUpdateStrategy();

	put(Property.POSITION_UPDATE_COUNTER, Int.valueOf(0));
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public AbstractParticle(AbstractParticle copy) {
        super(copy);

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
    public abstract AbstractParticle getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Fitness getBestFitness();

    /**
     * Get the best position of the {@code Particle}.
     * @return A {@link StructuredType} representing the {@code Particle}'s best position.
     */
    @Override
    public abstract StructuredType getBestPosition();

    /**
     * Get the velocity representation of the {@code Particle}.
     * @return A {@link StructuredType} representing the {@code Particle}'s velocity.
     */
    @Override
    public abstract StructuredType getVelocity();

    /**
     * Set the neighbourhood best particle for the current Particle based on the
     * topology of the current particle.
     *
     * @param particle  The particle to use as the current particle's
     *                  neighbourhood best particle.
     */
    @Override
    public abstract void setNeighbourhoodBest(Particle particle);

    /**
     * Get the current {@code Particle}'s neighbourhood best.
     * @return The neighbourhood best of the {@code Particle}
     */
    @Override
    public abstract Particle getNeighbourhoodBest();

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition(StructuredType newPosition) {
        put(Property.PREVIOUS_SOLUTION, getPosition());
        put(Property.CANDIDATE_SOLUTION, newPosition);
        
        int posUpdates = get(Property.POSITION_UPDATE_COUNTER).intValue();
        put(Property.POSITION_UPDATE_COUNTER, Int.valueOf(posUpdates + 1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateVelocity(StructuredType newVelocity) {
        put(Property.VELOCITY, newVelocity);
    }

    /**
     * Get the velocity {@link InitialisationStrategy}.
     * @return The current velocity {@linkplain InitialisationStrategy}.
     */
    @Override
    public InitialisationStrategy getVelocityInitialisationStrategy() {
        return this.velocityInitialisationStrategy;
    }

    /**
     * Set the velocity {@link InitialisationStrategy}.
     * @param initialisationStrategy The value to set.
     */
    @Override
    public void setVelocityInitialisationStrategy(InitialisationStrategy initialisationStrategy) {
        this.velocityInitialisationStrategy = initialisationStrategy;
    }

    /**
     * Get the current position {@link InitialisationStrategy}.
     * @return The current position {@linkplain InitialisationStrategy}.
     */
    public InitialisationStrategy<Particle> getPositionInitialisationStrategy() {
        return this.positionInitialisationStrategy;
    }

    /**
     * Set the position {@link InitialisationStrategy} to be used.
     * @param positionInitialisationStrategy The value to set.
     */
    public void setPositionInitialisationStrategy(InitialisationStrategy positionInitialisationStrategy) {
        this.positionInitialisationStrategy = positionInitialisationStrategy;
    }

    /**
     * Get the reference to the currently employed {@link NeighbourhoodBestUpdateStrategy}.
     * @return A reference to the current {@linkplain NeighbourhoodBestUpdateStrategy}
     */
    @Override
    public NeighbourhoodBestUpdateStrategy getNeighbourhoodBestUpdateStrategy() {
        return this.neighbourhoodBestUpdateStrategy;
    }

    /**
     * Set the {@link NeighbourhoodBestUpdateStrategy} to be used.
     * @param neighbourhoodBestUpdateStrategy   The {@link NeighbourhoodBestUpdateStrategy}
     *                                          to be used
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

    public void setPersonalBestInitialisationStrategy(InitialisationStrategy<Particle> personalBestInitialisationStrategy) {
        this.personalBestInitialisationStrategy = personalBestInitialisationStrategy;
    }
}
