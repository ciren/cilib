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
 * Multimodal2 function.
 *
 * Minimum: 0.0
 * R(0, 1)^1
 *
 */
public class MultimodalFunction2 implements ContinuousFunction {

    private static final long serialVersionUID = -5046586719830749372L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); i++) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * input.doubleValueOf(i)), 6.0);
            double exp1 = -2.0 * Math.log(2);
            double exp2 = Math.pow((input.doubleValueOf(i) - 0.1) / 0.8, 2.0);
            double y = Math.exp(exp1 * exp2);
            sum += x * y;
        }
        return sum;
    }
}
