/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.unconstrained.Weierstrass;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F16: Weierstrass Function
 */
public class BBOB16 extends AbstractBBOB {
	private IrregularFunctionDecorator irregular;
	private IllConditionedFunctionDecorator ill;
	private RotatedFunctionDecorator r, q;
	private Penalty pen;

	public BBOB16() {
		this.r = new RotatedFunctionDecorator();
		this.ill = Helper.newIllConditioned(1E-2, new Inner());
		this.q = Helper.newRotated(ill);
		this.irregular = Helper.newIrregular(q);
		this.pen = Helper.newPenalty(5);
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);

		r.setFunction(irregular);
		return r.apply(z) + (10.0 / input.size()) * pen.apply(input) + fOpt;
	}

	private class Inner implements ContinuousFunction {
		private Weierstrass weierstrass;

		public Inner() {
			this.weierstrass = new Weierstrass();
		}

		@Override
		public Double apply(Vector z) {
			r.setFunction(weierstrass);
			return r.apply(z);
		}
	}
}