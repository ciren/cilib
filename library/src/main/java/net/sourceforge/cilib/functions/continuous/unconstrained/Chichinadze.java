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
 * <p><b>Chichinadze.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <iSome New Test Functions for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical report, North-Eastern Hill University, India, 2006</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -43.31586206998933</li>
 * <li> <b>x</b>* = (5.90133, 0.5)</li>
 * <li> for x<sub>i</sub> in [-30,30]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * R(-30, 30)^2
 */
public class Chichinadze implements ContinuousFunction {

	@Override
	public Double apply(Vector input) {
		Preconditions.checkArgument(input.size() == 2, "Chichinadze function is only defined for 2 dimensions");

		double x1 = input.get(0).doubleValue();
		double x2 = input.get(1).doubleValue();

		double cos = Math.cos(Math.PI * x1 / 2);
		double sin = Math.sin(5*Math.PI*x1);
		double ePow = Math.exp(-0.5 * ((x2 - 0.5) * (x2 - 0.5)));

		return (x1*x1) - 12 * (x1) + 11 + 10*cos + 8 * sin - Math.pow((1/5.0), 0.5)* ePow;
	}
}
