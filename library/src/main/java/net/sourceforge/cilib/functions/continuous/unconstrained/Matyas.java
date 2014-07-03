/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import com.google.common.base.Preconditions;

/**
 * <p><b>Matyas.</b></p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0.0</li>
 * <li> <b>x</b>* = (0.0, 0.0)</li>
 * <li> for x<sub>i</sub> in [-10, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * </ul>
 * </p>
 *
 * R(-10, 10)^2
 *
 */
public class Matyas extends ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Matyas function is only defined for 2 dimensions");

        double x0 = input.doubleValueOf(0);
        double x1 = input.doubleValueOf(1);

        return 0.26 * (x0 * x0 + x1 * x1) - 0.48 * x0 * x1;
    }
}
