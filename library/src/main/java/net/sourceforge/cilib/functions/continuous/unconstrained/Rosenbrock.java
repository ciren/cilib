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
 * <p>Generalised Rosenbrock function.</p>
 *
 * <p><b>Reference:</b> X. Yao, Y. Liu, G. Liu, <i>Evolutionary Programming
 * Made Faster</i>,  IEEE Transactions on Evolutionary Computation,
 * 3(2):82--102, 1999</p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Non Separable</li>
 * <li>Continuous</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * f(x) = 0; x = 1
 *
 * x e [-2.048,2.048]
 *
 * R(-2.048, 2.048)^30
 *
 */
public class Rosenbrock implements ContinuousFunction {

    private static final long serialVersionUID = -5850480295351224196L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0;

        for (int i = 0; i < input.size()-1; ++i) {
            double a = input.doubleValueOf(i);
            double b = input.doubleValueOf(i+1);

            tmp += ((100 * (b - a * a) * (b - a * a)) + ((a - 1.0) * (a - 1.0)));
        }

        return tmp;
    }
}
