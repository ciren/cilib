/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Katsuura;
import net.sourceforge.cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F24 Lunacek bi-Rastrigin Function
 */
public class BBOB24 extends AbstractBBOB {
	private AsymmetricFunctionDecorator asymetric;
	private RotatedFunctionDecorator r, q;
	private Penalty pen;
	private double mu0, mu1, d;
	private UniformDistribution dist;

	public BBOB24() {
		q = Helper.newRotated(new Inner());
		asymetric = Helper.newAsymmetric(100, q);
		r = Helper.newRotated(asymetric);
		pen = new Penalty();
		dist = new UniformDistribution();
		mu0 = 2.5;
		d = 1.0;
	}

	@Override
	public Double apply(Vector input) {
		if (xOpt.size() != input.size()) {
			xOpt = Vector.fill(1, input.size());
			for (int i = 0; i < input.size(); i++) {
				xOpt.setReal(i, mu0 * Math.signum(dist.getRandomNumber() - 0.5));
			}
			fOpt = Helper.randomFOpt();
		}

		Vector xHat = Vector.sign(xOpt).multiply(input).multiply(2);

		double s = 1 - 1.0 / (2 * Math.sqrt(input.size() + 20) - 8.2);
		double mu1 = -Math.sqrt((mu0 * mu0 - d)/s);

		double sum1 = 0, sum2 = 0;
		for (int i = 0; i < input.size(); i++) {
			sum1 += Math.pow(xHat.doubleValueOf(i) - mu0, 2);
			sum2 += d * input.size() + s * Math.pow(xHat.doubleValueOf(i) - mu1, 2);
		}

		return Math.min(sum1, sum2) + r.apply(xHat.subtract(xOpt)) + 1E4 * pen.apply(input) + fOpt;
	}

	private class Inner implements ContinuousFunction {
		@Override
		public Double apply(Vector input) {
			double sum = 0;

			for (int i = 0; i < input.size(); i++) {
				sum += Math.cos(2 * Math.PI * input.doubleValueOf(i));
			}

			return 10 * (input.size() - sum);
		}
	}
}