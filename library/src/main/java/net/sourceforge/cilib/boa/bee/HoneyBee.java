/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Super interface for all types of bees in the artificial bee algorithm.
 */
public interface HoneyBee extends Entity {

    /**
     * {@inheritDoc}
     */
    @Override
    HoneyBee getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    Fitness getFitness();

    /**
     * Updates the position of the bee based on the neighbouring nectar content.
     */
    void updatePosition();

    /**
     * Gets the bee's position (contents).
     * @return the position.
     */
    Vector getPosition();

    /**
     * Sets the bee's position (contents).
     * @param position The value to set.
     */
    void setPosition(Vector position);

    /**
     * Getter method for the position update strategy.
     * @return the position update strategy.
     */
    BeePositionUpdateStrategy getPositionUpdateStrategy();

}
