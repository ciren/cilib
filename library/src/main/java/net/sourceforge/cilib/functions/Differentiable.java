/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public interface Differentiable {

    /**
     * Obtain the gradient {@code Vector} for the provided input {@code Vector}.
     * The gradient is, naturally, the derivative of the
     * {@link net.sourceforge.cilib.functions.Function} which implements this
     * interface.
     * @param x The provided input {@code Vector} to calculate the derivative at.
     * @return A {@link net.sourceforge.cilib.type.types.container.Vector} containing
     *         the gradient of the provided input.
     */
    Vector getGradient(Vector x);
}
