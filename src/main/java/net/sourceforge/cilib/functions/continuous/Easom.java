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
 * Easom function.
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Uni-modal</li>
 * </ul>
 *
 * f(x) = -1.0;  x = (Pi, Pi);
 *
 * @author  engel
 */
public class Easom extends ContinuousFunction {
	private static final long serialVersionUID = 7173528343222997045L;

	/** Creates a new instance of Easom. */
    public Easom() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-100, 100)^2");
    }

    /**
     * {@inheritDoc}
     */
    public Easom getClone() {
    	return new Easom();
    }

    /**
	 * Get the minimum of the function. It is defined to be a value of -1.0.
	 */
    public Object getMinimum() {
        return new Double(-1.0);
    }

    /**
     * {@inheritDoc}
     */
    public double evaluate(Vector x) {
    	double powerTerm1 = -((x.getReal(0)-Math.PI)*(x.getReal(0)-Math.PI));
    	double powerTerm2 = -((x.getReal(1)-Math.PI)*(x.getReal(1)-Math.PI));
    	double power = powerTerm1 + powerTerm2;
        return -Math.cos(x.getReal(0)) * Math.cos(x.getReal(1)) * Math.exp(power);
    }

}
