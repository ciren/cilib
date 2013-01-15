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
 * <p><b>Waves Function.</b></p>
 *
 * <p>Global Minimum:
 * <ul>
 * <li>f(x,y) = 0</li>
 * <li>Many global minima where ever x = 0</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Non-Separable</li>
 * <li>One global optimum, and nine local optima</li>
 * </ul>
 * </p>
 *
 * R(-0.9,1.2), R(-1.2,1.2)
 *
 */
public class Waves implements ContinuousFunction {
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Waves function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        return -(Math.pow(0.3*x, 3)-(y*y-4.5*y*y)*x*y-4.7*Math.cos(3*x-y*y*(2+x))*Math.sin(2.5*Math.PI*x));
    }
}
