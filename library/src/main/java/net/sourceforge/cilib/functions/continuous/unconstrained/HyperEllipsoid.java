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
 * <p><b>HyperEllipsoid.</b></p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-5.12,5.12]</li>
 * </ul>
 * </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * <li>Convex</li>
 * </ul>
 *
 * R(-5.12,5.12)^30
 * 
 */
public class HyperEllipsoid implements ContinuousFunction {

    private static final long serialVersionUID = 813261964413884141L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0;
        for (int i = 0; i < input.size(); ++i) {
            tmp += (i + 1) * input.doubleValueOf(i) * input.doubleValueOf(i);
        }
        return tmp;
    }
}
