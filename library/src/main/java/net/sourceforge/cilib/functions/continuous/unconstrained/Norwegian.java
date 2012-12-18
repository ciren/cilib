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
 * Norwegian function.
 */
public class Norwegian implements ContinuousFunction {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double product = 1.0;
        double value;

        for (int j = 0; j < input.size(); j++) {
            value = input.doubleValueOf(j);
            product *= Math.cos(Math.PI * Math.pow(value, 3)) * ((99 + value) / 100);
        }

        return new Double(product);
    }
}
