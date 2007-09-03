/*
 * Real.java
 * 
 * Created on Jun 13, 2005
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
package net.sourceforge.cilib.type.types;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import net.sourceforge.cilib.math.MathUtil;


/**
 * @author Gary Pampara
 */
public class Real extends Numeric {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5290504438178510485L;
	private double value;

	
	/**
	 * Create the instance with a random value
	 */
	public Real() {
		this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}
	
	
	public Real(double value) {
		this.value = value;
		this.setLowerBound(-Double.MAX_VALUE);
		this.setUpperBound(Double.MAX_VALUE);
	}
	
	
	/**
	 * Create the <code>Real</code> instance with the initial value which is random between <code>lower</code> and <code>upper</code>
	 * @param lower The lower boundary for the random number
	 * @param upper The upper boundary for the random number
	 */
	public Real(double lower, double upper) {
		double bottom = (lower == Double.NEGATIVE_INFINITY) ? -Double.MAX_VALUE : lower;
		double top = (upper == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : upper;
		value = (top-bottom)*MathUtil.random() + bottom;
		
		setLowerBound(lower);
		setUpperBound(upper);
	}
	
	
	public Real(Real copy) {
		this.value = copy.value;
		this.setLowerBound(copy.getLowerBound());
		this.setUpperBound(copy.getUpperBound());
	}
	
	
	/**
	 * 
	 */
	public Real clone() {
		return new Real(this);
	}


	/**
	 * 
	 */
	public boolean equals(Object other) {
		if (other instanceof Real) {
			Real i = (Real) other;
			
			if (this.value == i.value)
				return true;
		}
		
		return false;
	}

	
	/**
	 * 
	 */
	public int hashCode() {
		return Double.valueOf(this.value).hashCode();
	}
	
	

	@Override
	public void set(String value) {
		setReal(value);
	}
	

	@Override
	public void set(boolean value) {
		setBit(value);
	}


	@Override
	public void set(double value) {
		setReal(value);
	}


	@Override
	public void set(int value) {
		setInt(value);
	}


	/**
	 * 
	 */
	public boolean getBit() {
		return (this.value == 0.0) ? false : true;
	}

	
	/**
	 * 
	 */
	public void setBit(boolean value) {
		if (value == false)
			this.value = 0.0;
		else this.value = 1.0;
	}
	
	public void setBit(String value) {
		setBit(Boolean.parseBoolean(value));
	}

	
	/**
	 * 
	 */
	public int getInt() {
		return Double.valueOf(this.value).intValue();
	}

	
	/**
	 * 
	 */
	public void setInt(int value) {
		this.value = Integer.valueOf(value).doubleValue();
	}
	
	@Override
	public void setInt(String value) {
		setInt(Integer.parseInt(value));
	}

	
	/**
	 * 
	 */
	public double getReal() {
		return this.value;
	}

	
	/**
	 * 
	 */
	public void setReal(double value) {
		this.value = value;
	}
	
	public void setReal(String value) {
		setReal(Double.parseDouble(value));
	}

	
	/**
	 * 
	 */
	public int compareTo(Numeric other) {
		if (this.value == other.getReal())
			return 0;
		else 
			return (other.getReal() < value) ? 1 : -1;
	}
	
	/**
	 * Determine if the current value for this <tt>Real</tt> is defined within the lower and upper
	 * bounds, as specified by the domain of the problem.
	 * @return <tt>true</tt> if within the bounds, <tt>false</tt> otherwise.
	 */
	@Override
	public boolean isInsideBounds() {
		if (value >= this.getLowerBound() && value < this.getUpperBound())
			return true;

		return false;
	}

	/**
	 * Re-randomize the <code>Real</code> object based on the upper and lower bounds
	 */
	public void randomise() {
		this.value = (getUpperBound()-getLowerBound())*MathUtil.random() + getLowerBound();
	}
	
	
	/**
	 * Set the value of the <tt>Real</tt> to a default value of 0.0
	 */
	public void reset() {
		this.setReal(0.0);
	}
	
	
	/**
	 * Return a <code>String</code> representation of the <code>Real</code> object
	 * @return A <code>String</code> representing the object instance
	 */
	public String toString() {
		return String.valueOf(this.value);		
	}

	/**
	 * Get the type representation of this <tt>Real</tt> object as a string.
	 * 
	 * @return The String representation of this <tt>Type</tt> object.
	 */
	public String getRepresentation() {
		return "R(" + getLowerBound() + "," + getUpperBound() +")";
	}

	
	/**
	 * 
	 */
	public void writeExternal(ObjectOutput oos) throws IOException {
		oos.writeDouble(this.value);
	}

	
	/**
	 * 
	 */
	public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
		this.value = ois.readDouble();
	}
	
}
