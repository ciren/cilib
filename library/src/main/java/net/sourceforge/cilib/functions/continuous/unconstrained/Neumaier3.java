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
 * Neumaier3 (also Trid) function.
 *
 * <p>
 * Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = -n(n+4)(n-1)/6</li>
 * <li> <b>x</b>* = (n, 2(n+1-2), ...., i(n+1-i))</li>
 * <li> for x<sub>i</sub> in [-900,900]</li>
 * </ul>
 * </p>
 *
 * R(-900, 900)^30
 *
 */
public class Neumaier3 implements ContinuousFunction {

    private static final long serialVersionUID = 192809046725649930L;

    /**
     * {@inheritDoc}
     */
    public Double apply(Vector input) {
        double tmp1 = 0;
        double tmp2 = 0;
        for (int i = 0; i < input.size(); ++i) {
            tmp1 += (input.doubleValueOf(i) - 1) * (input.doubleValueOf(i) - 1);
        }
        for (int i = 1; i < input.size(); ++i) {
            tmp2 += input.doubleValueOf(i) * input.doubleValueOf(i - 1);
        }
        return tmp1 - tmp2;
    }
}
