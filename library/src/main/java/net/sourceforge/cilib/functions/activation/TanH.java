/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Hyperbolic Tangent Function.
 *
 */
public class TanH implements ActivationFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Real apply(Real input) {
        return Real.valueOf(apply(input.doubleValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double apply(double input) {
        double a = Math.exp(input);
        double b = Math.exp(-input);
        return ((a - b) / (a + b));
    }

    @Override
    public Vector getGradient(Vector x) {
        throw new RuntimeException("Implement me");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        throw new RuntimeException("Implement me");
    }

    /**
     * {@inheritDoc}
     * The active range is -Sqrt(3) - Sqrt(3), and Sqrt(3) = 1.732050808
     */
    @Override
    public double getLowerActiveRange() {
        return -1.732050808;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return 1.732050808;
    }

    @Override
    public TanH getClone() {
        return this;
    }
}
