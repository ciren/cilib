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
 * Easom function as taken from
 * www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -1.0</li>
 * <li> <b>x</b>* = (Pi, Pi)</li>
 * <li> for x<sub>i</sub> in [-100, 100]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Not separable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * R(-100, 100)^2
 *
 */
public class Easom implements ContinuousFunction {

    private static final long serialVersionUID = 7173528343222997045L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Easom function is only defined for 2 dimensions");

        double powerTerm1 = -((input.doubleValueOf(0)-Math.PI)*(input.doubleValueOf(0)-Math.PI));
        double powerTerm2 = -((input.doubleValueOf(1)-Math.PI)*(input.doubleValueOf(1)-Math.PI));
        double power = powerTerm1 + powerTerm2;
        return -Math.cos(input.doubleValueOf(0)) * Math.cos(input.doubleValueOf(1)) * Math.exp(power);
    }
}
