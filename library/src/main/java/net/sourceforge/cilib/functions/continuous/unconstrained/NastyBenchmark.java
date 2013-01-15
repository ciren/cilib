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
 * <p><b>NastyBenchmark.</b></p>
 *
 * Minimum: 0.0
 * R(-500, 500)^30
 *
 */
public class NastyBenchmark implements ContinuousFunction {

    private static final long serialVersionUID = 6848836780892359015L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0;
        for (int i = 0; i < input.size(); ++i) {
            double factor = (input.doubleValueOf(i) - (i + 1));
            tmp += factor * factor;
        }
        return tmp;
    }
}
