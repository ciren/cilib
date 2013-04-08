/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import java.util.List;
import com.google.common.collect.Lists;
import fj.F;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.bbob.Penalty;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.math.random.UniformDistribution;

/*
 * F21: Gallagher's Gaussian 101-me Peaks Function
 */
public class BBOB21 extends AbstractBBOB {
	private UniformDistribution dist;
	private Vector w;
	private List<Vector> c;
	private List<Vector> y;
	private int peaks;
	private Penalty pen;
	private Vector xLessY;
	private Vector currentC;
	private RotatedFunctionDecorator r;
	private F<Numeric, Numeric> irregularMapping;
	private Inner1 inner1;
	private Inner2 inner2;

	public BBOB21() {
		this.dist = new UniformDistribution();
		this.peaks = 101;
		this.pen = Helper.newPenalty(5);
		this.r = new RotatedFunctionDecorator();
		this.irregularMapping = new IrregularFunctionDecorator().getMapping();
		inner1 = new Inner1();
		inner2 = new Inner2();
	}

	@Override
	public Double apply(Vector input) {
		if (xOpt.size() != input.size()) {
			setup(input.size());
		}

		double max = Double.MIN_VALUE;

		for (int i = 0; i < peaks; i++) {
			double wi = w.doubleValueOf(i);

			xLessY = input.subtract(y.get(i));
			currentC = c.get(i);
			r.setFunction(inner1);

			max = Math.max(max, r.apply(xLessY));
		}

		Numeric finalValue = Real.valueOf(Math.pow(10 - max, 2));

		return irregularMapping.f(finalValue).doubleValue() + pen.apply(input) + fOpt;
	}

	private void setup(int size) {
		w = Vector.fill(10, peaks);
		y = Lists.newArrayList();
		y.add(Vector.fill(dist.getRandomNumber(-4.0, 4.0), size));
		for (int i = 1; i < peaks; i++) {
			y.add(Vector.fill(dist.getRandomNumber(-4.9, 4.9), size));
			w.setReal(i, 1.1 + 8 * ((i - 1) / (peaks - 2)));
		}
		xOpt = y.get(0).getClone();
		fOpt = Helper.randomFOpt();

		Vector alpha = Vector.fill(1, peaks - 1);
		for (int i = 0; i < peaks - 1; i++) {
			alpha.setReal(i, Math.pow(1000, 2 * (i / peaks - 2)));
		}
		alpha = Vector.newBuilder()
			.addAll(alpha.permute())
			.prepend(Real.valueOf(1000))
			.build();

		c = Lists.newArrayList();
		for (int i = 0; i < peaks; i++) {
			double smallA = alpha.doubleValueOf(i);
			Vector bigA = Vector.fill(1, size);
			for (int j = 0; j < size; j++) {
				double value = Math.pow(smallA, j * 0.5 / (size - 1));
				value /= Math.pow(smallA, 0.25);
				bigA.setReal(j, value);
			}
			c.add(bigA.permute());
		}
	}

	public void setPeaks(int peaks) {
		this.peaks = peaks;
	}

	private class Inner1 implements ContinuousFunction {
		@Override
		public Double apply(Vector input) {
			r.setFunction(inner2);
			return r.apply(input.multiply(currentC));
		}
	}

	private class Inner2 implements ContinuousFunction {
		@Override
		public Double apply(Vector input) {
			return -(1.0 / (2.0 * input.size())) * xLessY.dot(input);
		}
	}
}