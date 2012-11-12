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
 * SchwefelProblem2_21.
 *
 * Characteristics:
 *
 * f(x) = 0;
 *
 * x e [-100,100]
 *
 * R(-100, 100)^30
 *
 */
// TODO: Check discontinuous / continuous
public class SchwefelProblem2_21 implements ContinuousFunction {

    private static final long serialVersionUID = 8583159190281586599L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double max = Math.abs(input.doubleValueOf(0));
        double value;

        for (int i = 1; i < input.size(); ++i) {
            value = Math.abs(input.doubleValueOf(i));
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
