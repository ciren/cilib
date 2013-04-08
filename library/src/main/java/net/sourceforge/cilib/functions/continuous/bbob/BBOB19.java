/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F19: Composite Griewank-Rosenbrock Function, F8F2
 */
public class BBOB19 extends AbstractBBOB {
	private RotatedFunctionDecorator r;

	public BBOB19() {
		this.r = Helper.newRotated(new Inner());
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		return r.apply(input) + fOpt;
	}

	private class Inner implements ContinuousFunction {
		@Override
		public Double apply(Vector x) {
			double sum = 0;

			double factor = Math.max(1, Math.sqrt(x.size()) / 8);
			Vector z = x.multiply(factor).plus(Vector.fill(0.5, x.size()));

			for (int i = 0; i < z.size() - 1; i++) {
				double zi = z.doubleValueOf(i);
				double zi1 = z.doubleValueOf(i + 1);
				double si = 100 * Math.pow(zi * zi - zi1, 2) + Math.pow(zi - 1, 2);

				sum += si / 4000.0 - Math.cos(si);
			}

			return (10 / (x.size() - 1)) * sum + 10;
		}
	}
}