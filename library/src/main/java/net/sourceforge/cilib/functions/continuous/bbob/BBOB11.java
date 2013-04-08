/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.unconstrained.Discus;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F11: Discus Function
 */
public class BBOB11 extends AbstractBBOB {
	private RotatedFunctionDecorator r;
	private IrregularFunctionDecorator irregular;

	public BBOB11() {
		this.irregular = Helper.newIrregular(new Discus());
		this.r = Helper.newRotated(irregular);
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return r.apply(z) + fOpt;
	}
}