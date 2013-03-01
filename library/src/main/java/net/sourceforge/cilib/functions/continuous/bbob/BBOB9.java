/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rosenbrock;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F9: Rosenbrock Function, rotated
 */
public class BBOB9 extends AbstractBBOB {
	private RotatedFunctionDecorator r;

	public BBOB9() {
		this.r = Helper.newRotated(new Inner());
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		return r.apply(input) + fOpt;
	}

	private class Inner implements ContinuousFunction {
		private Rosenbrock rosenbrock;

		public Inner() {
			this.rosenbrock = new Rosenbrock();
		}

		@Override
		public Double apply(Vector x) {
			double factor = Math.max(1, Math.sqrt(x.size()) / 8);

			Vector z = x.multiply(factor).plus(Vector.fill(0.5, x.size()));
			return rosenbrock.apply(z);
		}
	}
}