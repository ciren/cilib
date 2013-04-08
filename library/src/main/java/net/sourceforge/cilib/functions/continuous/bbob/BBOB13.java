/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.type.types.container.Vector;

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
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.apply(z) + fOpt;
	}

	private class Inner implements ContinuousFunction {
		private Spherical sphere;

		public Inner() {
			this.sphere = new Spherical();
		}

		@Override
		public Double apply(Vector z) {
			return z.doubleValueOf(0) * z.doubleValueOf(0) + 100 *
				Math.sqrt(sphere.apply(z.copyOfRange(1, z.size())));
		}
	}
}