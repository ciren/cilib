/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.Entity;

/**
 * An initialization strategy the performs no actions.
 * @param <E> The entity type.
 */
public final class NullInitializationStrategy<E extends Entity> implements InitializationStrategy<E> {
    private static final long serialVersionUID = -1270509869246948001L;

    /**
     * {@inheritDoc}
     */
    @Override
    public InitializationStrategy getClone() {
        return this;
    }

    /**
     * This method does not perform any actions.
     *
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void initialize(Enum<?> key, E entity) {
        // Intentionally do nothing.
    }

}
