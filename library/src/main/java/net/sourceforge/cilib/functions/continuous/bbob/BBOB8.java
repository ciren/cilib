/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.continuous.unconstrained.Rosenbrock;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F8: Rosenbrock Function, original
 */
public class BBOB8 extends AbstractBBOB {
	private Rosenbrock rosenbrock;

	public BBOB8() {
		this.rosenbrock = new Rosenbrock();
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		double factor = Math.max(1, Math.sqrt(input.size()) / 8);

		Vector z = input.subtract(xOpt).multiply(factor).plus(Vector.fill(1, input.size()));
		return rosenbrock.apply(z) + fOpt;
	}
}