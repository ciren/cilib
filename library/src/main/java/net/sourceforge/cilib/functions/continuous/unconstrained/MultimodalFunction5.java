/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import com.google.common.base.Preconditions;

/**
 * Multimodal5 function..
 *
 * Minimum: 0.0
 * R(-6, 6)^2
 *
 */
public class MultimodalFunction5 extends ContinuousFunction {

    private static final long serialVersionUID = -8704025552791904890L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "MultimodalFunction5 is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return 200 - Math.pow((x*x + y - 11), 2) - Math.pow((x + y*y - 7), 2);
    }
}
