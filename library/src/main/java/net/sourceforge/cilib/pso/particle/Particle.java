/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.MemoryBasedEntity;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.pso.pbestupdate.PersonalBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Definition of a particle.
 */
public interface Particle extends Entity, SocialEntity, MemoryBasedEntity {

    /**
     * {@inheritDoc}
     */
    @Override
    Particle getClone();

    /**
     * Get the current position of the {@linkplain Particle}.
     * @return The {@linkplain Type} representing the position.
     */
    StructuredType getPosition();

    /**
     * Get the current velocity of the {@linkplain Particle}.
     * @return The {@linkplain Type} representing the velocity.
     */
    StructuredType getVelocity();

    StructuredType getGlobalGuide();

    StructuredType getLocalGuide();

    /**
     * Get the current best particle within the {@linkplain Particle}s neighbourhood.
     * @return The neighbourhood best particle.
     */
    Particle getNeighbourhoodBest();

    /**
     * Set the current neighbourhood best particle within the current neighbourhood.
     * @param particle The particle to set as the neighbourhood best.
     */
    void setNeighbourhoodBest(Particle particle);

    /**
     * Update the current position of the {@linkplain Particle}.
     */
    void updatePosition();

    /**
     * Update the velocity of the {@linkplain Particle}.
     */
    void updateVelocity();

    /**
     * Get the strategy that will be used to update the velocity.
     * @return The current {@linkplain VelocityProvider}.
     */
    VelocityProvider getVelocityProvider();

    /**
     * Set the {@linkplain VelocityProvider} to be used during velocity updates.
     * @param velocityProvider the {@linkplain VelocityProvider} to use.
     */
    void setVelocityProvider(VelocityProvider velocityProvider);

    /**
     * Get the strategy for the initialisation of the velocity.
     * @return The velocity {@linkplain InitialisationStrategy} that is currently set.
     */
    InitialisationStrategy getVelocityInitialisationStrategy();

    /**
     * Set the initialisation strategy for the {@linkplain Particle}s velocity.
     * @param velocityInitialisationStrategy The velocity initialisation strategy to set.
     */
    void setVelocityInitialisationStrategy(InitialisationStrategy velocityInitialisationStrategy);

    /**
     * Get the current {@linkplain PositionProvider} of the {@linkplain Particle}.
     * @return The current {@linkplain PositionProvider}.
     */
    PositionProvider getPositionProvider();

    /**
     * Set the {@linkplain PositionProvider} to use for particle position updates.
     * @param positionProvider The {@linkplain PositionProvider} to set.
     */
    void setPositionProvider(PositionProvider positionProvider);

    /**
     * Get the current strategy to perform personal best updates.
     * @return The current {@link PersonalBestUpdateStrategy}.
     */
    PersonalBestUpdateStrategy getPersonalBestUpdateStrategy();

    /**
     * Set the strategy to perform personal best updates.
     * @param personalBestUpdateStrategy The instance to set.
     */
    void setPersonalBestUpdateStrategy(PersonalBestUpdateStrategy personalBestUpdateStrategy);

    /**
     * Get the behavior associated with the current Particle.
     * @return The {@link ParticleBehavior} associated with the current Particle.
     */
    ParticleBehavior getParticleBehavior();

    /**
     * Set the behavior that the current particle should follow.
     * @param particleBehavior The {@linkplain ParticleBehavior} to use.
     */
    void setParticleBehavior(ParticleBehavior particleBehavior);

}
