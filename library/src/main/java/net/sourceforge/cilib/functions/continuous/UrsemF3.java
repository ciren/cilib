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
 * UrsemF3 function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * Minimum: 2.5
 * R(-2, 2)^2
 * 
 * @version 1.0
 */
public class UrsemF3 implements ContinuousFunction {

    private static final long serialVersionUID = -4477290008482842765L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        double result = Math.sin(2.2 * Math.PI * x + 0.5 * Math.PI) * ((2.0 - Math.abs(y)) / 2.0) * ((3.0 - Math.abs(x)) / 2.0);
        result += Math.sin(0.5 * Math.PI * y * y + 0.5 * Math.PI) * ((2.0 - Math.abs(y)) / 2.0) * ((2.0 - Math.abs(x)) / 2.0);

        return result;
    }
}
