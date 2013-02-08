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
 * Schwefel function as specified in CEC2013.
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
public class SchwefelCEC implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;

        for (int i = 0; i < input.size(); i++) {
            double x = input.doubleValueOf(i);
            if (x > 500) {
                sum += (500 - (x % 500))
                    * Math.sin(Math.sqrt(Math.abs(500 - (x % 500))))
                    - (Math.pow(x - 500, 2) / 10000 * input.size());
            } else if (x < -500) {
                sum += ((Math.abs(x) % 500) - 500)
                    * Math.sin(Math.sqrt(Math.abs((Math.abs(x) % 500) - 500)))
                    - (Math.pow(x + 500, 2) / 10000 * input.size());
            } else {
                sum += x * Math.sin(Math.pow(Math.abs(x), 0.5));
            }
        }
        return 418.9829 * input.size() - sum;
    }
}
