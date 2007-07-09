/*
 * UrsemF4.java
 *
 * Created on June 4, 2003, 1:46 PM
 *
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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */

public class UrsemF4 extends ContinuousFunction {
	private static final long serialVersionUID = 6177837410317967257L;

	public UrsemF4() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-2, 2)^2");
    }

    public Object getMinimum() {
        return new Double(1.5);
    }

    public double evaluate(Vector X) {
        double x = X.getReal(0);
        double y = X.getReal(1);
        double result = 3.0*Math.sin(0.5*Math.PI*x + 0.5*Math.PI)*(2.0 - Math.sqrt(x*x + y*y)/4.0);
        return result;
    }

    public void setDimension(int dimension) {
        throw new UnsupportedOperationException("setDimension(int) not supported by UrsemF4");
    }
}