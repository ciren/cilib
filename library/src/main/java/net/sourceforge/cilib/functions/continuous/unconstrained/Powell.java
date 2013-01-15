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
 * <p><b>Powell.</b></p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0.0</li>
 * <li> <b>x</b>* = (3, -1, 0, 1,..., 3, -1, 0, 1)</li>
 * <li> for x<sub>i</sub> in [-4, -5]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 4*N dimensions</li>
 * </ul>
 * </p>
 *
 * R(-4, -5)^32
 *
 */
public class Powell implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() % 4 == 0, "Powell function is only defined for 4*N dimensions");

        double sum = 0;

        for(int i = 1; i <= input.size() / 4; i++) {
            double x[] = new double[4];
            for(int j = 4; j >= 1; j--) {
                x[4 - j] = input.doubleValueOf(4 * i - j);
            }

            sum += Math.pow(x[0] + 10 * x[1], 2);
            sum += 5 * Math.pow(x[2] - x[3], 2);
            sum += Math.pow(x[1] - 2 * x[2], 4);
            sum += 10 * Math.pow(x[0] - x[3], 4);
        }

        return sum;
    }
}
