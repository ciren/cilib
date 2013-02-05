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
 * Katsuura function as specified in CEC2013.
 *
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 *
 */
public class Katsuura implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        int d = input.size();
        double product = 1;

        for (int i = 0; i < d; i++) {
            double sum = 0;
            for (int j = 1; j <= 32; j++) {
                double term = Math.pow(2, j) * input.doubleValueOf(i);
                sum += Math.abs(term - Math.round(term)) / Math.pow(2, j);
            }
            product *= Math.pow(1 + ((i + 1) * sum), 10.0 / Math.pow(d, 1.2));
        }
        return (10.0 / d * d) * product - (10.0 / d * d);
    }
}
