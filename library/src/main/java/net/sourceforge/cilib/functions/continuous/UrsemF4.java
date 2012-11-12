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
 * UrsemF4 function.
 *
 * <p>
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * R(-2, 2)^2
 * 
 * @version 1.0
 */
public class UrsemF4 implements ContinuousFunction {

    private static final long serialVersionUID = 6177837410317967257L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return 3.0 * Math.sin(0.5 * Math.PI * x + 0.5 * Math.PI) * (2.0 - Math.sqrt(x * x + y * y) / 4.0);
    }
}
