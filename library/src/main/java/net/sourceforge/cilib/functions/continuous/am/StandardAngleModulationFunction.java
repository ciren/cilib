/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.am;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The standard modulation function used in angle modulation problems.
 */
public class StandardAngleModulationFunction extends ContinuousFunction {
    public Double f(Vector input) {
        Preconditions.checkState(input.size() == 5, "This function is only defined for five dimensions.");
        
        double x = input.doubleValueOf(0);
        double a = input.doubleValueOf(1);
        double b = input.doubleValueOf(2);
        double c = input.doubleValueOf(3);
        double d = input.doubleValueOf(4);
        
        return Math.sin(2 * Math.PI * (x - a) * b * Math.cos(2 * Math.PI * c * (x - a))) + d;
    }
}
