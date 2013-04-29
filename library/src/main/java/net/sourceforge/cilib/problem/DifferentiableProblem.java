/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Represents problems that can provide first-order derivative information.
 */
public interface DifferentiableProblem {

    /**
     * Obtain the gradient {@code Vector} for the provided input {@code Vector}.
     * 
     * @param x The provided input {@code Vector} to calculate the derivative at.
     * @return A {@link net.sourceforge.cilib.type.types.container.Vector} containing
     *         the gradient of the provided input.
     */
    Vector getGradient(Vector x);
}
