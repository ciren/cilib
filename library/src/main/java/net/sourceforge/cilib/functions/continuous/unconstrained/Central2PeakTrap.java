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
 * <p><b>The Central Two-Peak Trap Function.</b></p>
 *
 * <p>Global Minimum:
 * <ul>
 * <li> f(x*) = -200</li>
 * <li> x* = 20</li>
 * <li> for x_i in [0, 20]</li>
 * </ul>
 * </p>
 * <p>Local Minimum:
 * <ul>
 * <li> f(x*) = -160</li>
 * <li> x* = 10</li>
 * <li> for x_i in [0, 20]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>One-dimensional only</li>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>One global minimum on the boundary, and a local minimum that may trap algorithms</li>
 * </ul>
 * </p>
 *
 * R(0,20)^1
 *
 */
public class Central2PeakTrap implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 1, "Central2PeakTrap function is only defined for 1 dimension");

        double x = input.doubleValueOf(0);

        if (x < 0)
            return 0.0;
        else if (x<=10)
            return -160/10*x;
        else if (x <= 15)
            return -160/5*(15-x);
        else if (x <= 20)
            return -200/5*(x-15);
        else
            return -200.0;
    }
}
