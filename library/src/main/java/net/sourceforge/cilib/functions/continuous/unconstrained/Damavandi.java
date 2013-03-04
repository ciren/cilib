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
 * <p>
 * The Damavandi function obtained from N. Damavandi, S. Safavi-Naeini,
 * "A hybrid evolutionary programming method for circuit optimization".
 *
 * <p>
 * Global Minimum: f(x,y) = 0;  (x,y) = (2, 2)
 * Note that if (x,y) = (2,2), then the function results in division by 0
 * Local Minimum: f(x,y) = 2; (x,y) = (7, 7)
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Non-separable</li>
 * <li>Deceptive</li>
 * </ul>
 * </p>
 *
 * R(0, 12)^2
 *
 */
public class Damavandi implements ContinuousFunction {

    private static final long serialVersionUID = 2857754134712271398L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Damavandi function is only defined for 2 dimensions");

        double x1 = input.doubleValueOf(0);
        double x2 = input.doubleValueOf(1);

        double numerator = Math.sin(Math.PI * (x1 - 2)) * Math.sin(Math.PI * (x2 - 2));
        double denumerator = Math.PI * Math.PI * (x1 - 2) * (x2 - 2);
        double factor1 = 1 - Math.pow(Math.abs(numerator / denumerator), 5);
        double factor2 = 2 + (x1 - 7) * (x1 - 7) + 2 * (x2 - 7) * (x2 - 7);

        return factor1 * factor2;
    }
}
