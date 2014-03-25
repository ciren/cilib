/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Yang Function 2
 * <p>
 * Reference:
 * <p>
 * X.S. Yang, "Firefly algorithm, stochastic test functions and design
 * optimisation." International Journal of Bio-Inspired Computation 2.2 (2010):
 * 78-84.
 * <p>
 *
 * f(x*) = 0; x* = (0,0,...,0)
 *
 * R(-2pi:2pi)^D
 */
public class Yang2 extends ContinuousFunction {
    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum1 = 0;
        double sum2 = 0;

        for (Numeric n : input) {
            double x = n.doubleValue();
            sum1 += Math.abs(x);
            sum2 += Math.sin(x * x);
        }

        return sum1 * Math.exp(-sum2);
    }
}
