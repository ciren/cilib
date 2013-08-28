/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.behaviour;

import net.sourceforge.cilib.entity.behaviour.AbstractBehaviour;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.positionprovider.StandardPositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;

/**
 * Behaviour representing normal particle behaviour.
 */
public class DoNothingBehaviour extends AbstractBehaviour {

    /**
     * Default constructor assigns standard position and velocity provider.
     */
    public DoNothingBehaviour() {
    }

    /**
     * Copy Constructor.
     *
     * @param copy The {@link StandardParticleBehaviour} object to copy.
     */
    public DoNothingBehaviour(DoNothingBehaviour copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DoNothingBehaviour getClone() {
        return new DoNothingBehaviour(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity performIteration(Entity entity) {

        //boundaryConstraint.enforce(entity);
        return entity;
    }

}
