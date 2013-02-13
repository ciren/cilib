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
 * UrsemF1 function.
 *
 * R(-2.5, 3)^2
 * Minimum: 4.81681
 *
 */
public class UrsemF1 extends ContinuousFunction {

    private static final long serialVersionUID = -2595919942608678319L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "UrsemF1 function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        return Math.sin(2.0 * x - 0.5 * Math.PI) + 3.0 * Math.cos(y) + 0.5 * x;
    }
}
