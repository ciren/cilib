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
 * Generalised Griewank function.
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multi-modal</li>
 * <li>Non-separable</li>
 * <li>Regular</li>
 * </ul>
 *
 * f(x) = 0; x = (0,0,...,0);
 * x_i e (-600,600)
 *
 * R(-600, 600)^30
 *
 */
public class Griewank implements ContinuousFunction {

    private static final long serialVersionUID = 1095225532651577254L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sumsq = 0;
        double prod = 1;
        for (int i = 0; i < input.size(); ++i) {
            sumsq += input.doubleValueOf(i) * input.doubleValueOf(i);
            prod *= Math.cos(input.doubleValueOf(i) / Math.sqrt(i + 1));
        }
        return 1 + sumsq * (1.0 / 4000.0) - prod;
    }
}
