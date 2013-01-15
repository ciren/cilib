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
 * <p><b>Zettle.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <iSome New Test Functions for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical report, North-Eastern Hill University, India, 2006</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -0.003791</li>
 * <li> <b>x</b>* = (-0.0299, 0)</li>
 * <li> for x<sub>i</sub> in [-5,5]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Unimodal</li>
 * <li>Non-Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * R(-5, 5)^2
 */
public class Zettle implements ContinuousFunction {

	@Override
	public Double apply(Vector input) {
		Preconditions.checkArgument(input.size() == 2, "Zettle function is only defined for 2 dimensions");

		double x1 = input.get(0).doubleValue();
        double x2 = input.get(1).doubleValue();
        return (Math.pow(((x1 * x1) + (x2 * x2) - 2*x1), 2) + 0.25*x1);
	}
}
