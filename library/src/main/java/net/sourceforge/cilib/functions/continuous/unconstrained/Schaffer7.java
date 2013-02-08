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
 * Schaffer's F7 function.
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
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-100, 100]</li>
 * </ul>
 * </p>
 *
 * R(-100.0,100.0)^10
 */
public class Schaffer7 implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;

        for (int i = 0; i < input.size() - 1; i++) {
            double x = input.doubleValueOf(i);
            double x1 = input.doubleValueOf(i + 1);
            double si = Math.sqrt(x * x + x1 * x1);

            double sinTerm = Math.sin(50 * Math.pow(si, 0.2));
            sum += Math.sqrt(si) + Math.sqrt(si) * sinTerm * sinTerm;
        }

        sum /= input.size() - 1;
        return sum * sum;
    }
}
