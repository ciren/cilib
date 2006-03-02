/*
 * BinaryAdapter.java
 * 
 * Created on Feb 28, 2006
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
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

/**
 * Class to convert a binary vector into a continuous vector for optimisation of
 * continous problems by a binary optimiser.
 * 
 * This still needs some experimental work though, to verify that it is working
 * 
 * @author Gary Pampara
 */
public class BinaryAdapter extends DiscreteFunction {
	
	private Function function;
	private int bitsPerDimension;
	
	/**
	 * Constructor.
	 */
	public BinaryAdapter() {
		
	}

	
	/**
	 * Evaluate the {@see net.sourceforge.cilib.type.types.Vector} by
	 * decoding the binary vector into a continuous vector and evaluate the results
	 * by feeding the result into the wrapped funtion.
	 * 
	 * @param vector The {@see net.sourceforge.cilib.type.types.Bit} vector to evaluate
	 */
	@Override
	public double evaluate(Vector vector) {
		Vector decodedVector = this.decodeBitString(vector);
		
		return function.evaluate(decodedVector);
	}
	
	/**
	 * 
	 */
	public Object getMinimum() {
		return function.getMinimum();
	}
	
	
	/**
	 * 
	 */
	public Object getMaximum() {
		return function.getMaximum();
	}
	
	
	
	/**
	 * @return Returns the bitsPerDimension.
	 */
	public int getBitsPerDimension() {
		return bitsPerDimension;
	}

	/**
	 * @param bitsPerDimension The bitsPerDimension to set.
	 */
	public void setBitsPerDimension(int bitsPerDimension) {
		if (bitsPerDimension < 0)
			throw new RuntimeException("Cannot set the amount of bits in BinaryAdapter to anything < 0");
		
		this.bitsPerDimension = bitsPerDimension;
	}

	/**
	 * @return Returns the function.
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param function The function to set.
	 */
	public void setFunction(Function function) {
		this.function = function;
	}
	

	/**
	 * 
	 * @param bits
	 * @return
	 */
	public Vector decodeBitString(Vector bits) {
		Vector vector = new MixedVector();
		
		for (int i = 0; i < bits.getDimension(); ) {
			double tmp = valueOf(bits, i, i+this.bitsPerDimension);
			
			vector.append(new Real(tmp));
			i += this.bitsPerDimension;
		}
		
		return vector;
	}

	
	/**
	 * 
	 * @param str
	 * @param i
	 * @param j
	 * @return
	 */
	public double valueOf(Vector vector, int i, int j) {
		double result = 0.0;
		int n = 1;
		
		for (int counter = j-1; counter >= i; counter--) {
			if (vector.getBit(counter) == true) {
				result += n;
			}
			
			n = n*2;
		}

		return result;
	}

}
