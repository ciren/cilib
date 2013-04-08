/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.random.generator.Rand;

/*
 * F5: Linear Slope Function
 */
public class BBOB5 extends AbstractBBOB {

	@Override
	public Double apply(Vector input) {
		int size = input.size();

		if (xOpt.size() != size) {
			xOpt = Vector.fill(1, size);
			for (int i = 0; i < size; i++) {
				xOpt.setReal(i, 5 * Math.signum(Rand.nextDouble() - 0.5));
			}
			fOpt = Helper.randomFOpt();
		}

		double sum = 0;

		for (int i = 0; i < size; i++) {
			double xi = input.doubleValueOf(i);
			double xopti = xOpt.doubleValueOf(i);

			double si = Math.signum(xopti) * Math.pow(10, i / (size - 1));
			double zi = xopti * xi < 25 ? xi : xopti;

			sum += 5 * Math.abs(si) - si * zi;
		}

		return sum + fOpt;
	}
}