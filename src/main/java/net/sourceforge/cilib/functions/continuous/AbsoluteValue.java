/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The absolute value function.
 * @author Olusegun Olorunda
 */
public class AbsoluteValue extends ContinuousFunction {
	private static final long serialVersionUID = 1662988096338786773L;

	/**
	 * Create an instance of {@linkplain AbsoluteValue}. Domain is defaulted to R(-100, 100)^30.
	 */
	public AbsoluteValue() {
		setDomain("R(-100, 100)^30");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbsoluteValue getClone() {
		return new AbsoluteValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getMinimum() {
		return new Double(0);
	 }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double evaluate(Vector x) {
		double tmp = 0;
		for (int i = 0; i < getDimension(); ++i) {
			tmp += Math.abs(x.getReal(i));
		}
		return tmp;
	}

}
