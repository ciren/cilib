/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Interface to return the known optimum of a function.
 */
public interface KnownOptimum<T> {
    /**
     * Return the known optimum function value.
     * @return The function's optimum value.
     */
    T getOptimum();
}
