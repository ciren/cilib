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
 * <p><b>Hartman6.</b></p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -3.32</li>
 * <li> <b>x</b>* = (0.201, 0.150, 0.476, 0.275, 0.311, 0.657)</li>
 * <li> for x<sub>i</sub> in [0, 1]</li>
 * </ul>
 * </p>
 * R(0,1)^6
 *
 */
public class Hartman6 implements ContinuousFunction {

    private final double A[][] = {
        {10.0, 3.00, 17.0, 3.50, 1.70, 8.00},
        {0.05, 10.0, 17.0, 0.10, 8.00, 14.0},
        {3.00, 3.50, 1.70, 10.0, 17.0, 8.00},
        {17.0, 8.00, 0.05, 10.0, 0.10, 14.0}
    };

    private final double c[] = {1.0, 1.2, 3.0, 3.2};

    private final double p[][] = {
        {0.1312, 0.1696, 0.5569, 0.0124, 0.8283, 0.5886},
        {0.2329, 0.4135, 0.8307, 0.3736, 0.1004, 0.9991},
        {0.2348, 0.1451, 0.3522, 0.2883, 0.3047, 0.6650},
        {0.4047, 0.8828, 0.8732, 0.5743, 0.1091, 0.0381}
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 6, "Hartman6 function is only defined for 6 dimensions");

        double outerSum = 0.0;

        for (int i = 0; i < 4; i++) {
            double innerSum = 0.0;
            for(int j = 0; j < input.size(); j++) {
                innerSum += A[i][j] * Math.pow(input.doubleValueOf(j) - p[i][j], 2);
            }
            outerSum += c[i] * Math.exp(-innerSum);
        }

        return -outerSum;
    }
}
