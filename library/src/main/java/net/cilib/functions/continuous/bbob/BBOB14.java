/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.bbob;

import net.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.cilib.functions.continuous.unconstrained.DifferentPowers;
import net.cilib.type.types.container.Vector;

/*
 * F14: Different Powers Function
 */
public class BBOB14 extends AbstractBBOB {
	private RotatedFunctionDecorator r;

	public BBOB14() {
		this.r = Helper.newRotated(new DifferentPowers());
	}

	@Override
	public Double f(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.f(z) + fOpt;
	}
}