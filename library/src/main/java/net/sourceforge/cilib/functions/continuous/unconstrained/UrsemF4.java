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
 * UrsemF4 function.
 *
 * R(-2, 2)^2
 *
 */
public class UrsemF4 extends ContinuousFunction {

    private static final long serialVersionUID = 6177837410317967257L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "UrsemF4 function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return 3.0 * Math.sin(0.5 * Math.PI * x + 0.5 * Math.PI) * (2.0 - Math.sqrt(x * x + y * y) / 4.0);
    }
}
