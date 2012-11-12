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
 * <p><b>The Quartic function.</b></p>
 * <p>Source: Evolutionary Programming Made Faster (Xin Yao)</p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-1.28,1.28]</li>
 * </ul>
 * </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * </ul>
 * </p>
 *
 * R(-1.28, 1.28)^30
 *
 */
public class Quartic implements ContinuousFunction {

    private static final long serialVersionUID = 1830591489569513511L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double result = 0.0;

        for (int i = 0; i < input.size(); i++) {
            double square = input.doubleValueOf(i) * input.doubleValueOf(i);
            double square2 = square * square;

            result += (i + 1) * square2;
        }

        return result;
    }
}
