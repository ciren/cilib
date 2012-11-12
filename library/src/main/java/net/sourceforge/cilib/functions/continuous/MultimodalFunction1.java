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
 * Minimum: 0.0
 * R(0, 1)^1
 */
public class MultimodalFunction1 implements ContinuousFunction {

    private static final long serialVersionUID = -5261002551096587662L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double dResult = 0.0;
        for (int i = 0; i < input.size(); ++i) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * input.doubleValueOf(i)), 6.0);
            dResult += x;
        }
        return dResult;
    }
}
