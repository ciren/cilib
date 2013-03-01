/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Schwefel;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.random.UniformDistribution;

/*
 * F20: Schwefel Function
 */
public class BBOB20 extends AbstractBBOB {
	private Vector uniform;
	private IllConditionedFunctionDecorator ill;
	private UniformDistribution dist;

	public BBOB20() {
		this.ill = Helper.newIllConditioned(10, new Inner());
		this.dist = new UniformDistribution();
	}

	@Override
	public Double apply(Vector input) {
		int size = input.size();
		if (xOpt.size() != size) {
			uniform = Vector.fill(1, size);
			for (int i = 0; i < size; i++) {
				uniform.setReal(i, Math.signum(dist.getRandomNumber() - 0.5));
			}
			xOpt = Vector.fill(4.2096874633 / 2.0, size).multiply(uniform);
			fOpt = Helper.randomFOpt();
		};

		Vector xHat = input.multiply(uniform).multiply(2);
		Vector zHat = xHat.getClone();
		for (int i = 0; i < size - 1; i++) {
			zHat.setReal(i + 1, xHat.doubleValueOf(i + 1)
				+ 0.25 * (xHat.doubleValueOf(i) - xOpt.doubleValueOf(i)));
		}

		return ill.apply(zHat.subtract(xOpt)) + fOpt;
	}

	private class Inner implements ContinuousFunction {
		private Penalty pen;
		private Schwefel schwefel;

		public Inner() {
			this.pen = Helper.newPenalty(5.0);
			this.schwefel = new Schwefel();
		}

		@Override
		public Double apply(Vector zHat) {
			Vector z = zHat.plus(xOpt).multiply(100);

			return (schwefel.apply(z) / z.size()) + 100 * pen.apply(z.multiply(0.01));
		}
	}
}