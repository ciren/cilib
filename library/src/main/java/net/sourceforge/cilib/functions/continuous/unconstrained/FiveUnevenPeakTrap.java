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
 * <p><b>The Five Uneven Peak Trap Function.</b></p>
 *
 * <p>Global Minimum:
 * <ul>
 * <li> f(x*) = -200</li>
 * <li> x* = 0, x* = 30</li>
 * <li> for x_i in [0, 30]</li>
 * </ul>
 * </p>
 * <p>Local Minimum:
 * <ul>
 * <li> Three local minima at 5, 12.5, 22.5</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 1 dimension</li>
 * <li>Multimodal</li>
 * <li>Separable</li>
 * <li>Two global minima, one at each boundary, and three local minima that may trap algorithms</li>
 * </ul>
 * </p>
 *
 * R(0,30)^1
 *
 */
public class FiveUnevenPeakTrap implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 1, "FiveUnevenPeakTrap function is only defined for 1 dimension");

        double x = input.doubleValueOf(0);

        if (x < 0)
            return 0.0;
        else if (x < 2.5)
            return -80*(2.5-x);
        else if (x < 5)
            return -64*(x-2.5);
        else if (x < 7.5)
            return -64*(7.5-x);
        else if (x < 12.5)
            return -28*(x-7.5);
        else if (x < 17.5)
            return -28*(17.5-x);
        else if (x < 22.5)
            return -32*(x-17.5);
        else if (x < 27.5)
            return -32*(27.5-x);
        else if (x <= 30)
            return -80*(x-27.5);
        else
            return 0.0;
    }
}
