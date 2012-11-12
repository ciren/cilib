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
 * SchwefelProblem2_22.
 *
 * Characteristics:
 *
 * f(x) = 0;
 *
 * x e [-10,10]
 *
 * R(-10, 10)^30
 *
 */
// TODO: Check discontinuous / continuous
public class SchwefelProblem2_22 implements ContinuousFunction {

    private static final long serialVersionUID = -5004170862929300400L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;
        double product = 0;
        for (int i = 0; i < input.size(); ++i) {
            sum += Math.abs(input.doubleValueOf(i));
            if (i == 0) {
                product = Math.abs(input.doubleValueOf(i));
            } else {
                product *= Math.abs(input.doubleValueOf(i));
            }
        }

        return sum + product;
    }
}
