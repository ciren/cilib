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
 * A continuous function with a single peak around x=0.
 * Defined as 1/(1+x^2)
 * This function is similar to the Gaussian function. Its peak is a bit sharper and it
 * does not converge onto its asymptotes as quickly. Its active zone is fairly close
 * to that of the sigmoid function.
 */
public class Peak extends ActivationFunction {

    public Peak() {
    }

    public Peak(Peak copy) {
    }

    @Override
    public Peak getClone() {
        return new Peak(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real f(Real input) {
        return Real.valueOf(f(input.doubleValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double f(double input) {
        return 1/(1+Math.pow(input, 2));
    }

    @Override
    public Vector getGradient(Vector x) {
        return Vector.of(this.getGradient(x.doubleValueOf(0)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        return -2*number/Math.pow((1+Math.pow(number, 2)), 2);
    }

    /**
     * {@inheritDoc}
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
}
