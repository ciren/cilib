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
 * SixHumpCamelBack function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Non Separable</li>
 * </ul>
 * </p>
 *
 * f(x) = -1.0316; x = (-0.0898, 0.1726); x = (0.0898, -0.1726)
 * x_1 e [-3, 3]; x_2 e [-2, 2]
 *
 * R(-3,3),R(-2,2)
 *
 * @version 1.0
 */
public class SixHumpCamelBack implements ContinuousFunction {

    private static final long serialVersionUID = -3834640752316926216L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "SixHumpCamelBack function is only defined for 2 dimensions");

        double x1 = input.doubleValueOf(0);
        double x2 = input.doubleValueOf(1);

        return (4 - 2.1 * x1 * x1 + Math.pow(x1, 4.0) / 3.0) * x1 * x1 + x1 * x2 + 4 * (x2 * x2 - 1) * x2 * x2;
    }
}
