/*
 * NoisyFunction.java
 * 
 * Created on Oct 21, 2005
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
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * 
 * @author Gary Pampara
 *
 */
public class NoisyFunctionDecorator extends ContinuousFunction {
	private static final long serialVersionUID = -3918271655104447420L;
	
	private ContinuousFunction function;
	private RandomNumber randomNumber;

	public NoisyFunctionDecorator() {
		setDomain("R");
		randomNumber = new RandomNumber();
	}
	
	@Override
	public NoisyFunctionDecorator getClone() {
		return new NoisyFunctionDecorator();
	}
	
	/**
	 * 
	 */
	public double evaluate(Vector x) {
		return function.evaluate(x) + randomNumber.getGaussian(); 
	}
	
	
	/**
	 * @return Returns the noisyFunction.
	 */
	public ContinuousFunction getFunction() {
		return function;
	}

	
	/**
	 * @param noisyFunction The noisyFunction to set.
	 */
	public void setFunction(ContinuousFunction function) {
		this.function = function;
		this.setDomain(function.getDomainRegistry().getDomainString());
	}
	

}
