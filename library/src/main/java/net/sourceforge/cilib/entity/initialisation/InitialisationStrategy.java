/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.util.Cloneable;

/**
 *
 */
public interface InitialisationStrategy<E> extends Cloneable {

    @Override
    InitialisationStrategy getClone();

    void initialise(Enum<?> key, E entity);

}
