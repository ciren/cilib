/*
 * ContinuousFunction.java
 * 
 * Created on Jul 24, 2004
 *
 * Copyright (C) 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.functions;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Edwin Peer
 */
public abstract class ContinuousFunction extends Function {
	protected double verticalShift = 0.0;
	protected double horizontalShift = 0.0;

	public ContinuousFunction() {
		// constraint.add(new ContentValidator(new TypeValidator(Real.class)));
	}

	public ContinuousFunction(ContinuousFunction copy) {
		verticalShift = copy.verticalShift;
		horizontalShift = copy.horizontalShift;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.Functions.Function#getMinimum()
	 */
	public Object getMinimum() {
		return new Double(-Double.MAX_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.Functions.Function#getMaximum()
	 */
	public Object getMaximum() {
		return new Double(Double.MAX_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.Functions.Function#evaluate(java.lang.Object)
	 */
	public Double evaluate(Object x) {
		Double d = null;
		try {
			d = new Double(evaluate((Vector) x));
		}
		catch (ClassCastException c) {
			c.printStackTrace();
		}

		return d;
	}

	public abstract double evaluate(Vector x);

	/**
	 * Get the horizontal shift (X-axis) associated with this function
	 * @return The horizontal shift in the X-axis
	 */
	public double getHorizontalShift() {
		return horizontalShift;
	}

	/**
	 * Set the amount of horizontal shift to be applied to the function during evaluation.
	 * @param horizontalShift The amount of horizontal shift.
	 */
	public void setHorizontalShift(double horizontalShift) {
		this.horizontalShift = horizontalShift;
	}

	/**
	 * Get the vertical shift (Y-axis) associated with this function
	 * @return The vertical shift in the Y-axis
	 */
	public double getVerticalShift() {
		return verticalShift;
	}

	/**
	 * Set the amount of vertical shift to be applied to the function during evaluation.
	 * @param verticalShift the amount of vertical shift.
	 */
	public void setVerticalShift(double verticalShift) {
		this.verticalShift = verticalShift;
	}
}
