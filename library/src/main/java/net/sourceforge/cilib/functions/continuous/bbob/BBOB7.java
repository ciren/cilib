/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Elliptic;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F7: Step Ellipsoidal Function
 */
public class BBOB7 extends AbstractBBOB {
	private RotatedFunctionDecorator r;
	private IllConditionedFunctionDecorator ill;
	private Penalty pen;

	public BBOB7() {
		this.ill = Helper.newIllConditioned(10, new StepEllipsoidal());
		this.r = Helper.newRotated(ill);
		this.pen = Helper.newPenalty(5);
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.apply(z) + pen.apply(input) + fOpt;
	}

	private class StepEllipsoidal implements ContinuousFunction {
		private Elliptic elliptic;
		private RotatedFunctionDecorator q;

		public StepEllipsoidal() {
			this.elliptic = new Elliptic();
			this.elliptic.setConditionNumber(1E2);
			this.q = Helper.newRotated(elliptic);
		}

		@Override
		public Double apply(Vector zHat) {
			Vector zCurve = Vector.fill(1, zHat.size());
			for (int i = 0; i < zHat.size(); i++) {
				double zi = zHat.doubleValueOf(i);
				double floored = Math.floor(0.5 + zi);
				zCurve.setReal(i, zi > 0.5 ? floored : floored / 10.0);
			}

			return 0.1 * Math.max(Math.abs(zHat.doubleValueOf(0) / 1E4), q.apply(zCurve));
		}
	}
}