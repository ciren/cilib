/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.random.generator.Rand;

/**
 * Yang Function 4
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
 * R(-5:5)^D
 */
public class Yang4 extends ContinuousFunction {
    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum = 0;
        for (int i = 0; i < input.size(); i++) {
            sum += Rand.nextDouble()
                * Math.pow(Math.abs(input.doubleValueOf(i)), i + 1);
        }
        return sum;
    }
}
