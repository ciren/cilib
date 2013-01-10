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
 * Multimodal3 function.
 *
 * Minimum: 0.0
 * R(0, 1)^1
 *
 */
public class MultimodalFunction3 implements ContinuousFunction {

    private static final long serialVersionUID = 3687474318232647359L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); ++i) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(input.doubleValueOf(i), 0.75) - 0.05)), 6.0);
            sum += x;
        }
        return sum;
    }
}
