/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.iterator;

import java.util.List;
import java.util.ListIterator;
import net.sourceforge.cilib.util.Cloneable;

/**
 * TODO: Complete this Javadoc.
 *
 * @param <E> The type.
 */
public interface AlgorithmIterator<E> extends ListIterator<E>, Cloneable {

    @Override
    AlgorithmIterator<E> getClone();

    E current();

    void setAlgorithms(List<E> algorithms);
}
