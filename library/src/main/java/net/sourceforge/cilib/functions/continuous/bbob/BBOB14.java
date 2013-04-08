/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.unconstrained.DifferentPowers;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F14: Different Powers Function
 */
public class BBOB14 extends AbstractBBOB {
	private RotatedFunctionDecorator r;

	public BBOB14() {
		this.r = Helper.newRotated(new DifferentPowers());
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.apply(z) + fOpt;
	}
}