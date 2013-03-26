/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff.firefly;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.ff.positionupdatestrategies.FireflyPositionUpdateStrategy;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Interface for all types of fireflies used in the firefly algorithm.
 */
public interface Firefly extends Entity {

    /**
     * {@inheritDoc}
     */
    @Override
    Firefly getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    Fitness getFitness();

    /**
     * Updates the position of the firefly based on a neighbouring firefly.
     */
    void updatePosition(Firefly other);

    /**
     * Gets the firefly's position.
     * @return the position of the firefly.
     */
    Vector getPosition();

    /**
     * Sets the firefly's position.
     * @param position The value to set.
     */
    void setPosition(Vector position);

    /**
     * Getter method for the position update strategy.
     * @return the position update strategy.
     */
    FireflyPositionUpdateStrategy getPositionUpdateStrategy();

    /**
     * Compare the intensity of the current instance to another {@link Firefly}.
     * A default implementation is making the intensity equal or proportional
     * to the fitness of the firefly.
     */
    int compareIntensity(Firefly other);

    /**
     * Compares intensity to determine if the current {@link Firefly} is brighter
     * than a neighbouring firefly.
     */
    boolean isBrighter(Firefly other);
}
