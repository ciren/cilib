/*
 * LinearIncreasingUpdateStrategy.java
 *
 * Created on March 18, 2004, 4:23 PM
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.controlparameterupdatestrategies;

import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * @author Gary Pampara
 */
public class LinearIncreasingControlParameter extends BoundedControlParameter {
	private static final long serialVersionUID = -6813625954992761973L;

	/**
	 *
	 */
	public LinearIncreasingControlParameter() {
		super();
	}

	/**
	 * @param copy
	 */
	public LinearIncreasingControlParameter(LinearIncreasingControlParameter copy) {
		super(copy);
	}

	/**
	 * 
	 */
	public LinearIncreasingControlParameter clone() {
		return new LinearIncreasingControlParameter(this);
	}

	/**
	 * 
	 */
	public void update() {
		double result = getLowerBound() + (getUpperBound() - getLowerBound()) * Algorithm.get().getPercentageComplete();
		parameter.setReal(result);
	}

	@Override
	public void setLowerBound(double lower) {
		super.setLowerBound(lower);
		this.parameter.setReal(lower);
	}
	
}
