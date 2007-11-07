/*
 * InvertedFunction.java
 *
 * Created on May 19, 2006
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
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Function implementation to accept a function and to return the reciprocal
 * of that function value.
 * 
 * @author Gary Pampara
 */
public class InvertedFunctionDecorator extends ContinuousFunction {
	private static final long serialVersionUID = -7506823207533866371L;
	
	private Function function;

	@Override
	public double evaluate(Vector x) {
		double innerFunctionValue = function.evaluate(x);
		
		if (innerFunctionValue == 0)
			throw new ArithmeticException("Inner function evaluation equated to 0. Division by zero is undefined");
		
		return (1.0 / innerFunctionValue);
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
		this.setDomain(function.getDomainRegistry().getDomainString());
	}

}
