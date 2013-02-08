/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>Different Powers.</b></p>
 *
 * <p>
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 *
 */
public class DifferentPowers implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;

        for (int i = 0; i < input.size(); i++) {
            double power = 2 + ((4 * i) / (input.size() - 1));
            sum += Math.pow(Math.abs(input.doubleValueOf(i)), power);
        }

        return Math.sqrt(sum);
    }
}
