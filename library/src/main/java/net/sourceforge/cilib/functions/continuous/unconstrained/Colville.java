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
 * <p><b>The Colville Function.</b></p>
 *
 * <p><b>Reference:</b> Doo-Hyun Choi, Cooperative mutation based evolutionary programming for continuous function optimization, Operations Research Letters, Volume 30, Issue 3, June 2002, Pages 195-201/p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (1, 1, 1, 1)</li>
 * <li> for x_i in [-10, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 4 dimensions</li>
 * <li>Unimodal</li>
 * <li>Non-separable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * R(-10, 10)^4
 *
 */
public class Colville implements ContinuousFunction {

    private static final long serialVersionUID = 4561331100889232057L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 4, "Colville function is only defined for 4 dimensions");

        double a = input.doubleValueOf(0);
        double b = input.doubleValueOf(1);
        double c = input.doubleValueOf(2);
        double d = input.doubleValueOf(3);

        return 100 * (a - b * b) * (a - b * b)
                + (1 - a) * (1 - a)
                + 90 * (d - c * c) * (d - c * c)
                + (1 - c) * (1 - c)
                + 10.1 * ((b - 1) * (b - 1) + (d - 1) * (d - 1))
                + 19.8 * (b - 1) * (d - 1);
    }
}
