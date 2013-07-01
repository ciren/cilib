/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.bbob;

import net.cilib.functions.ContinuousFunction;
import net.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.cilib.functions.continuous.unconstrained.Spherical;
import net.cilib.type.types.container.Vector;

/*
 * F13: Sharp Ridge Function
 */
public class BBOB13 extends AbstractBBOB {
	private RotatedFunctionDecorator r, q;
	private IllConditionedFunctionDecorator ill;

	public BBOB13() {
		this.q = Helper.newRotated(new Inner());
		this.ill = Helper.newIllConditioned(10, q);
		this.r = Helper.newRotated(ill);
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.f(z) + fOpt;
	}

	private class Inner extends ContinuousFunction {
		private Spherical sphere;

		public Inner() {
			this.sphere = new Spherical();
		}

		@Override
		public Double f(Vector z) {
			return z.doubleValueOf(0) * z.doubleValueOf(0) + 100 *
				Math.sqrt(sphere.f(z.copyOfRange(1, z.size())));
		}
	}
}