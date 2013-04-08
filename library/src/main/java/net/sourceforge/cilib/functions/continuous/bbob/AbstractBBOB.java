/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.bbob;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.KnownOptimum;
import net.sourceforge.cilib.type.types.container.Vector;

public abstract class AbstractBBOB implements ContinuousFunction, KnownOptimum<Double>  {
	protected Vector xOpt;
	protected double fOpt;

	public AbstractBBOB() {
		xOpt = Vector.of();
	}

	@Override
	public Double getOptimum() {
		return this.fOpt;
	}

	/**
	 * Initialise the horizontal and vertical shifts if needed.
	 * @param size The size of the input vector.
	 */
	protected void initialise(int size) {
		if (xOpt.size() != size) {
			xOpt = Helper.randomXOpt(size);
			this.fOpt = Helper.randomFOpt();
		}
	}

	/**
	 * Get the shifted global optimium used by this function.
	 * @return The shifted global optimum.
	 */
	public Vector getXOpt() {
		return this.xOpt;
	}
}