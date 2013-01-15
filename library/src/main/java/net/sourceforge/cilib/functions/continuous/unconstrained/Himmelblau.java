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
 * The Himmelblau function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * </ul>
 *
 * R(-6, 6)^2
 *
 * @version 1.0
 */
public class Himmelblau implements ContinuousFunction {

    private static final long serialVersionUID = 7323733640884766707L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Himmelblau function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return Math.pow((x * x + y - 11), 2) + Math.pow((x + y * y - 7), 2);
    }
}
