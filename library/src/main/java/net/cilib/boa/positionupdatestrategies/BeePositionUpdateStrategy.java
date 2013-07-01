/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.boa.positionupdatestrategies;

import net.cilib.boa.bee.HoneyBee;
import net.cilib.util.Cloneable;


/**
 * Interface for a bee position update strategy.
 *
 */
public interface BeePositionUpdateStrategy extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    BeePositionUpdateStrategy getClone();

    /**
     * Updates the position of the given bee.
     * @param bee the bee the position update is for.
     * @param other another bee that the position update might use to update the position.
     * @return whether the position update was successful.
     */
    boolean updatePosition(HoneyBee bee, HoneyBee other);

}
