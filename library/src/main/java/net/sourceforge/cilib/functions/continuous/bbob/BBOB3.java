/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.continuous.decorators.IrregularFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.AsymmetricFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F3: Rastrigin Function
 */
public class BBOB3 extends AbstractBBOB {
	private IrregularFunctionDecorator irregular;
	private AsymmetricFunctionDecorator asymmetric;
	private IllConditionedFunctionDecorator ill;

	public BBOB3() {
		this.ill = Helper.newIllConditioned(10, new Rastrigin());
		this.asymmetric = Helper.newAsymmetric(0.2, ill);
		this.irregular = Helper.newIrregular(asymmetric);
	}

	@Override
	public Double apply(Vector input) {
		initialise(input.size());

		Vector z = input.subtract(xOpt);
		return irregular.apply(z) + fOpt;
	}
}