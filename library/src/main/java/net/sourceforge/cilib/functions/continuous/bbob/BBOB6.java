/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import fj.F;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F6: Attractive Sector Function
 */
public class BBOB6 extends AbstractBBOB {
	private RotatedFunctionDecorator r, q;
	private IllConditionedFunctionDecorator ill;

	public BBOB6() {
		this.q = Helper.newRotated(new Sector());
		this.ill = Helper.newIllConditioned(10, q);
		this.r = Helper.newRotated(ill);
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.apply(z) + fOpt;
	}

	private class Sector implements ContinuousFunction {
		F<Numeric, Numeric> irregularMapping;

		public Sector() {
			irregularMapping = new IrregularFunctionDecorator().getMapping();
		}

		@Override
		public Double apply(Vector z) {
			double sum = 0;

			for (int i = 0; i < z.size(); i++) {
				double zi = z.doubleValueOf(i);
				double si = zi * xOpt.doubleValueOf(i) > 0 ? 100 : 1;

				sum += si * si * zi * zi;
			}

			return irregularMapping.f(Real.valueOf(Math.pow(sum, 0.9))).doubleValue();
		}
	}
}