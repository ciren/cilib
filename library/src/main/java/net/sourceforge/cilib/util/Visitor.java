/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util;

/**
 * Abstract class defining the general structure of a <tt>Visitor</tt>.
 *
 * @param <E> The type object.
 */
public interface Visitor<E> {

    /**
     * Visit the provided object.
     * @param o The object to visit.
     */
    void visit(E o);

    /**
     * Determine if the visitor has completed its visit operation.
     * @return <code>true</code> if the visit operation is complete,
     *         <code>false</code> otherwise.
     */
    boolean isDone();
}
