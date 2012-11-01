/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.util.Cloneable;

/**
 *
 */
public interface InitializationStrategy<E> extends Cloneable {

    @Override
    InitializationStrategy getClone();

    void initialize(Enum<?> key, E entity);

}
