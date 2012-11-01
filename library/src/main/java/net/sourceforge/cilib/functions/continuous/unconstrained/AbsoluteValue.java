/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *  <p><b>The Absolute Value Function.</b></p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x_i in [-100,100]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * R(-100, 100)^30
 *
 */
public class AbsoluteValue implements ContinuousFunction {

    private static final long serialVersionUID = 1662988096338786773L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0.0;
        for (int i = 0; i < input.size(); ++i) {
            tmp += Math.abs(input.doubleValueOf(i));
        }
        return tmp;
    }
}
