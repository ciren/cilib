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
 * <p><b>Bird Function</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle Swarm Methods</i>
 * North-Eastern Hill University, India, 2002</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -106.764537 (approx) </li>
 * <li> <b>x</b>* = (??,??)</li>
 * <li> for x<sub>1</sub>, x<sub>2</sub> in [-2*&pi;, 2*&pi;]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Unimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * R(-6.285714286,6.285714286)^2
 *
 */
public class Bird implements ContinuousFunction {

    private static final long serialVersionUID = -7803711986955989075L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Bird function is only defined for 2 dimensions");

        double x1 = input.doubleValueOf(0);
        double x2 = input.doubleValueOf(1);

        return Math.sin(x1) * Math.exp((1 - Math.cos(x2)) * (1 - Math.cos(x2))) + Math.cos(x2) * Math.exp((1 - Math.sin(x1)) * (1 - Math.sin(x1))) + (x1 - x2) * (x1 - x2);
    }
}
