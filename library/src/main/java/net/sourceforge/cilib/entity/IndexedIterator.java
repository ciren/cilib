/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import java.util.Iterator;

/**
 * Interface to define the manner in which the iterator is to be constructed for Array types.
 *
 * @param <T> The {@linkplain Entity} type.
 */
public interface IndexedIterator<T extends Entity> extends Iterator<T> {
    public int getIndex();
}
