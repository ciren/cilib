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
 * Minimum for domain: 0.0
 * R(0, 1)^1
 */
public class MultimodalFunction4 implements ContinuousFunction {

    private static final long serialVersionUID = -957215773660609565L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double dResult = 0.0;
        for (int i = 0; i < input.size(); i++) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(input.doubleValueOf(i), 0.75) - 0.05)), 6.0);
            double exp1 = -2.0 * Math.log(2);
            double exp2 = Math.pow((input.doubleValueOf(i) - 0.08) / 0.854, 2.0);
            double y = Math.exp(exp1 * exp2);
            dResult += x * y;
        }
        return dResult;
    }
}
