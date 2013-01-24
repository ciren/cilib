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
 * <p><b>Hartman3.</b></p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -3.8756</li>
 * <li> <b>x</b>* = (0.1, 0.55592003, 0.85218259)</li>
 * <li> for x<sub>i</sub> in [0, 1]</li>
 * </ul>
 * </p>
 * R(0,1)^3
 *
 */
public class Hartman3 implements ContinuousFunction {

    private final double A[][] = {
        {3.0, 10.0, 30.0},
        {0.1, 10.0, 35.0},
        {3.0, 10.0, 30.0},
        {0.1, 10.0, 30.5}
    };

    private final double c[] = {1.0, 1.2, 3.0, 3.2};

    private final double p[][] = {
        {0.36890, 0.1170, 0.2673},
        {0.46990, 0.4387, 0.7470},
        {0.10910, 0.8732, 0.5547},
        {0.03815, 0.5743, 0.8828}
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 3, "Hartman3 function is only defined for 3 dimensions");

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
