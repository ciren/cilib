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
 * TODO: Complete this javadoc.
 *
 * R(-100.0, 100.0)^6
 */
public class Step implements ContinuousFunction {

    private static final long serialVersionUID = -3888436745417400797L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;

        for (int i = 0; i < input.size(); i++) {
            double val = Math.floor(input.doubleValueOf(i) + 0.5);
            sum += val * val;
        }

        return sum;
    }
}
