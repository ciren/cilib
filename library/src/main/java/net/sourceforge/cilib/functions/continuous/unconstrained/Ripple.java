/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Ripple function.
 *
 * Minimum: 2.2
 * R(0, 1)^2
 *
 */
public class Ripple implements ContinuousFunction {

    private static final long serialVersionUID = 2956377362140947929L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Ripple function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        double term1 = Math.exp(-1.0 * Math.log(2) * Math.pow(((x - 0.1) / 0.8), 2));
        double term2 = Math.pow(Math.sin(5 * Math.PI * x), 6) + 0.1 * Math.pow(Math.cos(500 * Math.PI * x), 2);
        double term3 = Math.exp(-2.0 * Math.log(2) * Math.pow(((y - 0.1) / 0.8), 2));
        double term4 = Math.sin(5 * Math.PI * y) + 0.1 * Math.pow(Math.cos(500 * Math.PI * y), 2);

        return term1 * term2 + term3 * term4;
    }
}
