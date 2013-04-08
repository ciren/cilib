/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.unconstrained.BentCigar;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F12: Bent Cigar Function
 */
public class BBOB12 extends AbstractBBOB {
	private RotatedFunctionDecorator r;
	private AsymmetricFunctionDecorator asymmetric;

	public BBOB12() {
		this.r = new RotatedFunctionDecorator();
		this.asymmetric = Helper.newAsymmetric(0.5, new Inner());
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);

		r.setFunction(asymmetric);
		return r.apply(z) + fOpt;
	}

	private class Inner implements ContinuousFunction {
		private BentCigar bentCigar;

		public Inner() {
			this.bentCigar = new BentCigar();
		}

		@Override
		public Double apply(Vector input) {
			r.setFunction(bentCigar);
			return r.apply(input);
		}
	}
}