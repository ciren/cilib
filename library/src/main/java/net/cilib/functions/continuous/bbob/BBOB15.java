/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.bbob;

import net.cilib.functions.ContinuousFunction;
import net.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import net.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.cilib.functions.continuous.unconstrained.Rastrigin;
import net.cilib.type.types.container.Vector;

/*
 * F15: Rastrigin Function
 */
public class BBOB15 extends AbstractBBOB {
	private IrregularFunctionDecorator irregular;
	private AsymmetricFunctionDecorator asymmetric;
	private IllConditionedFunctionDecorator ill;
	private RotatedFunctionDecorator r, q;

	public BBOB15() {
		this.r = new RotatedFunctionDecorator();
		this.ill = Helper.newIllConditioned(10, new Inner());
		this.q = Helper.newRotated(ill);
		this.asymmetric = Helper.newAsymmetric(0.2, q);
		this.irregular = Helper.newIrregular(asymmetric);
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);

		r.setFunction(irregular);
		return r.f(z) + fOpt;
	}

	private class Inner extends ContinuousFunction {
		private Rastrigin rastrigin;

		public Inner() {
			this.rastrigin = new Rastrigin();
		}

		@Override
		public Double f(Vector z) {
			r.setFunction(rastrigin);
			return r.f(z);
		}
	}
}