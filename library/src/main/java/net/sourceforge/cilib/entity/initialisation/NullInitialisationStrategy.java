/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.Entity;

/**
 * An initialisation strategy the performs no actions.
 * @param <E> The entity type.
 */
public final class NullInitialisationStrategy<E extends Entity> implements InitialisationStrategy<E> {
    private static final long serialVersionUID = -1270509869246948001L;

    /**
     * {@inheritDoc}
     */
    @Override
    public InitialisationStrategy getClone() {
        return this;
    }

    /**
     * This method does not perform any actions.
     *
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void initialise(Enum<?> key, E entity) {
        // Intentionally do nothing.
    }

}
