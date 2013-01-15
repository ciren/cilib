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
 * <p><b>Beale Function.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle Swarm Methods</i>
 * North-Eastern Hill University, India, 2002</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (3, 0.5)</li>
 * <li> for x<sub>1</sub>, x<sub>2</sub> in [-4.5, 4.5]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Unimodal</li>
 * <li>Separable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * R(-4.5,4.5)^2
 *
 */
public class Beale implements ContinuousFunction {

    private static final long serialVersionUID = -7803711986955989075L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Beale function is only defined for 2 dimensions");

        double x1 = input.doubleValueOf(0);
        double x2 = input.doubleValueOf(1);
        return (1.5 - x1 + x1 * x2) * (1.5 - x1 + x1 * x2) + (2.25 - x1 + x1 * x2 * x2) * (2.25 - x1 + x1 * x2 * x2) + (2.625 - x1 + x1 * x2 * x2 * x2) * (2.625 - x1 + x1 * x2 * x2 * x2);
    }
}

