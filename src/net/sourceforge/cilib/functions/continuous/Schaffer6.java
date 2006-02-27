/*
 * Schaffer6.java
 * 
 * Created on Jan 19, 2006
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
 *
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

/**
 * The Schaffer F6 function
 * 
 * Minimum: f(x,y) = 0; (x,y) = (0,0);
 * 
 * <p> 
 * Characteristics:
 * <ul>
 * <li>Need to complete</li>
 * </ul>
 * 
 * <p>
 * LaTeX function code: <br>
 * 
 * \textbf{Schaffer F6:} $f(x) = 0.5 + \frac{(sin^{2} \sqrt{x^2 + y^2}) - 0.5}{(1.0 + 0.001(x^2 + y^2))^2}$ 
 * 
 * @author Gary Pampara
 */
public class Schaffer6 extends ContinuousFunction {
	
	/**
	 * Constructor. Initialise the function to the initial domain of R(-100.0,100.0)^2
	 */
	public Schaffer6() {
		setDomain("R(-100.0,100.0)^2");
	}
	
	
	/**
	 * Initialise the function minimum value.
	 * @return The minimum value as a <tt>Double</tt> object with value of 0.0 
	 */
	public Object getMinimum() {
		return new Double(0.0);
	}

	
	/**
	 * Evaluate the funcntion and return the evaluation
	 * 
	 * @param x The input vector to the function
	 * @return A double value representing the function evaluation
	 */
	public double evaluate(Vector x) {
		double X = x.getReal(0);
		double Y = x.getReal(1);
		
		double squared = X*X + Y*Y;
		double squareRooted = Math.sqrt(squared);
		
		double numerator = (Math.sin(squareRooted) * Math.sin(squareRooted)) - 0.5;
		
		double denominatorTmp = 1.0 + 0.001*squared;
		double denominator = denominatorTmp * denominatorTmp;

		return 0.5 + (numerator / denominator);
	}

}
