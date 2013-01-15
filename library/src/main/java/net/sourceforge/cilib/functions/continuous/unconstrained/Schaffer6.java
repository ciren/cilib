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
 * <p><b>Schaffer's F6 generalised, also referred to as the Pathological Function.</b></p>
 *
 * <p><b>Reference:</b> S. Rahnamayan, H. R. Tizhoosh, M. M. A. Salama <i>A novel population initialisation method for accelerating evolutionary algorithms</i>,
 * Computers and Mathematics with Applications, 2007</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-100, 100]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * </ul>
 * </p>
 *
 * R(-100.0,100.0)^30
 */
public class Schaffer6 implements ContinuousFunction {

    private static final long serialVersionUID = 4959662717057274057L;

    /**
     * Evaluate the function and return the evaluation.
     *
     * @param input The input vector to the function
     * @return A double value representing the function evaluation
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;
        
        for (int i = 0; i < input.size()-1; i++) {
            double xi = input.doubleValueOf(i);
            double xj = input.doubleValueOf(i + 1);
            double sinSquared = Math.sin(Math.sqrt((100 * (xi*xi)) + (xj*xj)));
            sinSquared *= sinSquared;

            double squaredVal = (xi * xi) - (2 * xi * xj) + (xj * xj);
            squaredVal *= squaredVal;

            sum += 0.5 + ((sinSquared - 0.5) / (1 + (0.001 * squaredVal)));
        }

        return sum;
    }
}
