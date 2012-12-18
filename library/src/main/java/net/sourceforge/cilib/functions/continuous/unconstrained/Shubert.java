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
 * <p><b>The Shubert Function.</b></p>
 *
 * <p>Global Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = -186.7309088</li>
 * <li> Many global minima: n=1 has 3, n=2 has 9, n=3 has 81, n=4 has 324, n has pow(3,n)</li>
 * <li> All unevenly spaced</li>
 * <li> for x<sub>i</sub> in [-10,10]</li>
 * </ul>
 * </p>
 * 
 * <p>Local Minimum:
 * <ul>
 * <li> Many local minima</li>
 * </ul>
 * </p>
 * 
 * <p>Characteristics:
 * <ul>
 * <li>Multi-dimensional</li>
 * <li>Multimodal</li>
 * <li>Non-Separable</li>
 * </ul>
 * </p>
 *
 */
public class Shubert implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double result = 1.0;
        for (int i = 0; i < input.size(); ++i) {
            double result2 = 0.0;
            for (int j = 1; j <= 5; j++) {
                result2 += j*Math.cos((j+1)*input.doubleValueOf(i) + j);
            }
            result *= result2;
        }
        return result;
    }
}
