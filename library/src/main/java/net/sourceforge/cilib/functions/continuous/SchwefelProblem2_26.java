/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * SchwefelProblem 2_26.
 *
 * Characteristics:
 *
 * f(x) = -12569.5, x = (420.9687,...,420.9687);
 *
 * x e [-500,500]
 *
 * R(-500, 500)^30
 *
 */
// TODO: Check discontinuous / continuous
public class SchwefelProblem2_26 implements ContinuousFunction {

    private static final long serialVersionUID = -4483598483574144341L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;

        for (int i = 0; i < input.size(); i++) {
            sum += input.doubleValueOf(i)*Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i))));
        }
        return -sum;
    }
}

