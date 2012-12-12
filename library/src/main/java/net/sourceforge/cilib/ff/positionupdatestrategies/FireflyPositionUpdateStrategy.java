/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff.positionupdatestrategies;

import net.sourceforge.cilib.ff.firefly.Firefly;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;


/**
 * Interface for a firefly position update strategy.
 */
public interface FireflyPositionUpdateStrategy extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    FireflyPositionUpdateStrategy getClone();

    /**
     * Updates the position of the given firefly with respect to a
     * neighbouring firefly.
     * @param firefly the firefly the position update is for.
     * @param other another firefly whose position and attraction is used
     * @return A {@link Vector} containing the updated position.
     */
    Vector updatePosition(Firefly firefly, Firefly other);
}
