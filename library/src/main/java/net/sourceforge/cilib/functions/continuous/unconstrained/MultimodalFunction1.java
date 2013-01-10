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
 * Multimodal1 function.
 *
 * Minimum: 0.0
 * R(0, 1)^1
 *
 */
public class MultimodalFunction1 implements ContinuousFunction {

    private static final long serialVersionUID = -5261002551096587662L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); ++i) {
            sum += Math.pow(Math.sin(5.0 * Math.PI * input.doubleValueOf(i)), 6.0);
        }
        return sum;
    }
}
