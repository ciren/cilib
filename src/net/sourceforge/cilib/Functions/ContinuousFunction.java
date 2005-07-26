/*
 * ContinuousFunction.java
 * 
 * Created on Jul 24, 2004
 *
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
package net.sourceforge.cilib.Functions;

import net.sourceforge.cilib.Domain.Real;
import net.sourceforge.cilib.Domain.Validator.ContentValidator;
import net.sourceforge.cilib.Domain.Validator.TypeValidator;
import net.sourceforge.cilib.Type.Types.Vector;

/**
 * @author espeer
 */
public abstract class ContinuousFunction extends Function {

	public ContinuousFunction() {
        constraint.add(new ContentValidator(new TypeValidator(Real.class)));
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Functions.Function#getMinimum()
	 */
	public Object getMinimum() {
		return new Double(- Double.MAX_VALUE);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Functions.Function#getMaximum()
	 */
	public Object getMaximum() {
		return new Double(Double.MAX_VALUE);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Functions.Function#evaluate(java.lang.Object)
	 */
	public Comparable evaluate(Object x) {
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
}
