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
 * <p><b>The Vincent Function.</b></p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multi-dimensional only</li>
 * <li>Multimodal</li>
 * <li>Non-Separable</li>
 * <li>for n=1, 6 global minima and no local minima</li>
 * <li>for n=2, 36 global minima and no local minima</li>
 * </ul>
 * </p>
 *
 * R(0.25,10)^n
 *
 */
public class Vincent implements ContinuousFunction {
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double result = 1.0;
        for (int i = 0; i < input.size(); ++i)
            result += Math.sin(10*Math.log(input.doubleValueOf(i)));
        return -result;
    }
}
