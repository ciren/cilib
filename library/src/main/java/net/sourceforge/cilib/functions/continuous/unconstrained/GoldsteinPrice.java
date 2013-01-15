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
 * The Goldstein-Price function.<br><br>
 *
 * Minimum: f(x) = 3; x = (0, -1)<br><br>
 *
 * -2 &lt;= x &lt;= 2<br><br>
 *
 * R(-2, 2)^2
 *
 *
 */
public class GoldsteinPrice implements ContinuousFunction {

    private static final long serialVersionUID = 5635493177950325746L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Goldstein-Price function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        double part1 = 1 + (x + y + 1.0) * (x + y + 1.0) * (19.0 - 14.0 * x + 3 * x * x - 14 * y + 6 * x * y + 3 * y * y);
        double part2 = 30 + (2 * x - 3 * y) * (2 * x - 3 * y) * (18 - 32 * x + 12 * x * x + 48 * y - 36 * x * y + 27 * y * y);
        return part1 * part2;
    }
}
