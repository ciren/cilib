/*
 * ExponentiallyDecreasingUpdateStrategy.java
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.Algorithm;

public class ExponentiallyDecreasingControlParameter extends BoundedControlParameter {
	private static final long serialVersionUID = 2703195595193249266L;

	public ExponentiallyDecreasingControlParameter() {
		super();
	}
	
	/**
	 * Copy constructor
	 * @param copy
	 */
	public ExponentiallyDecreasingControlParameter(ExponentiallyDecreasingControlParameter copy) {
		super(copy);
	}
	
	@Override
	public ExponentiallyDecreasingControlParameter getClone() {
		return new ExponentiallyDecreasingControlParameter(this);
	}

	@Override
	public void update() {
		double result = Math.exp((-1) * Algorithm.get().getPercentageComplete());
		this.parameter.setReal(result);
	}

	@Override
	public void setUpperBound(double value) {
		super.setUpperBound(value);
		this.parameter.setReal(value);
	}
	
}
