/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * f(x) = 0.0 (Minimum)
 * R(-100, 100)^2
 */
public class Schaffer2 implements ContinuousFunction {

    private static final long serialVersionUID = 7289010453718555694L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum_squares = input.doubleValueOf(0) * input.doubleValueOf(0) + input.doubleValueOf(1) * input.doubleValueOf(1);
        double term1 = Math.pow(sum_squares, 0.25);
        double term2 = Math.pow(50 * Math.pow(sum_squares, 0.1), 2) + 1;
        return term1 * term2;
    }
}
