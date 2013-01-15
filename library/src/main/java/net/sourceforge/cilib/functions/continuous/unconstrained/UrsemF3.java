/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * UrsemF3 function.
 *
 * Minimum: 2.5
 * R(-2, 2)^2
 *
 */
public class UrsemF3 implements ContinuousFunction {

    private static final long serialVersionUID = -4477290008482842765L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "UrsemF3 function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        double result = Math.sin(2.2 * Math.PI * x + 0.5 * Math.PI) * ((2.0 - Math.abs(y)) / 2.0) * ((3.0 - Math.abs(x)) / 2.0);
        result += Math.sin(0.5 * Math.PI * y * y + 0.5 * Math.PI) * ((2.0 - Math.abs(y)) / 2.0) * ((2.0 - Math.abs(x)) / 2.0);

        return result;
    }
}
