/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * F22 Gallagher's Gaussian 21-me Peaks Function
 */
public class BBOB22 extends AbstractBBOB {
	private BBOB21 bbob21;

	public BBOB22() {
		bbob21 = new BBOB21();
		bbob21.setPeaks(21);
	}

	@Override
	public Double apply(Vector input) {
		double value = bbob21.apply(input);

		this.xOpt = bbob21.getXOpt();
		this.fOpt = bbob21.getOptimum();

		return value;
	}
}