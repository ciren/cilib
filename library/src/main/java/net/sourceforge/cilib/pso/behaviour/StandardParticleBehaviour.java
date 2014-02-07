/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.behaviour;

import net.sourceforge.cilib.entity.behaviour.AbstractBehaviour;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.positionprovider.StandardPositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;

/**
 * Behaviour representing normal particle behaviour. The behaviour is:
 * 1-update velocity
 * 2-update position
 * 3-enforce boundary constraints
 * 4-calculate fitness
 */
public class StandardParticleBehaviour extends AbstractBehaviour {

    private PositionProvider positionProvider;
    private VelocityProvider velocityProvider;

    /**
     * Default constructor assigns standard position and velocity provider.
     */
    public StandardParticleBehaviour() {
        this.positionProvider = new StandardPositionProvider();
        this.velocityProvider = new StandardVelocityProvider();
    }

    /**
     * Copy Constructor.
     *
     * @param copy The {@link StandardParticleBehaviour} object to copy.
     */
    public StandardParticleBehaviour(StandardParticleBehaviour copy) {
        super(copy);

        this.positionProvider = copy.positionProvider.getClone();
        this.velocityProvider = copy.velocityProvider.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardParticleBehaviour getClone() {
        return new StandardParticleBehaviour(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Particle performIteration(Entity entity) {

		((Particle) entity).updateVelocity(velocityProvider.get((Particle) entity));
        ((Particle) entity).updatePosition(positionProvider.get((Particle) entity));

        boundaryConstraint.enforce(entity);
        
        entity.updateFitness(fitnessCalculator.getFitness(entity));
        
        return (Particle) entity;
    }
    
    /**
     * Get the currently set {@link PositionProvider}.
     *
     * @return The current {@link PositionProvider}.
     */
    public PositionProvider getPositionProvider() {
        return positionProvider;
    }

    /**
     * Set the {@link PositionProvider}.
     *
     * @param positionProvider The {@link PositionProvider} to set.
     */
    public void setPositionProvider(PositionProvider positionProvider) {
        this.positionProvider = positionProvider;
    }

    /**
     * Get the currently set {@link VelocityProvider}.
     *
     * @return The current {@link VelocityProvider}.
     */
    public VelocityProvider getVelocityProvider() {
        return this.velocityProvider;
    }

    /**
     * Set the {@link VelocityProvider}.
     *
     * @param velocityProvider The {@link VelocityProvider} to set.
     */
    public void setVelocityProvider(VelocityProvider velocityProvider) {
        this.velocityProvider = velocityProvider;
    }
}
