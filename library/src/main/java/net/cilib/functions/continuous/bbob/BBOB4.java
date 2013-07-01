/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.bbob;

import net.cilib.functions.ContinuousFunction;
import net.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.cilib.functions.continuous.unconstrained.Rastrigin;
import net.cilib.type.types.container.Vector;

/*
 * F4: Buche-Rastrigin Function
 */
public class BBOB4 extends AbstractBBOB {
	private IrregularFunctionDecorator irregular;

	public BBOB4() {
		this.irregular = Helper.newIrregular(new Buche());
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return irregular.f(z) + fOpt;
	}

	private class Buche extends ContinuousFunction {
		private Rastrigin rastrigin;

		public Buche() {
			this.rastrigin = new Rastrigin();
		}

		@Override
		public Double f(Vector input) {
			Vector.Builder builder = Vector.newBuilder();

        	for (int i = 0; i < input.size(); i++) {
	            double x = input.doubleValueOf(i);
	            double s = Math.pow(10, 0.5 * (i / (input.size()-1)));

	            if ((x > 0) && (i % 2 == 0)) {
	                builder.add(10 * s * x);
	            } else {
	                builder.add(s * x);
	            }
        	}

        	return rastrigin.f(builder.build());
		}
	}
}