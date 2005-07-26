/*
 * Real.java
 * 
 * Created on Jun 13, 2005
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
package net.sourceforge.cilib.Type.Types;


/**
 * @author gary
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Real extends Numeric {
	private double value;

	/**
	 * Create the instance with a random value
	 */
	public Real() {
		this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}
	
	public Real(double value) {
		this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		this.value = value;
	}
	
	/**
	 * Create the <code>Real</code> instance with the initial value which is random between <code>lower</code> and <code>upper</code>
	 * @param lower The lower boundary for the random number
	 * @param upper The upper boundary for the random number
	 */
	public Real(double lower, double upper) {
		double bottom = (lower == Double.NEGATIVE_INFINITY) ? -Double.MAX_VALUE : lower;
		double top = (upper == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : upper;
		value = (top-bottom)*Math.random() + bottom;
		
		setLowerBound(lower);
		setUpperBound(upper);
	}


	/**
	 * Create the <code>Real</code> instance with the initial value which is random between <code>lower</code> and <code>upper</code>
	 * @param lower The lower boundary for the random number
	 * @param upper The upper boundary for the random number
	 * @return A <code>Real</code> object instance initialised between lower and upper
	 */
	public static Real buildReal(double lower, double upper) {
		return new Real(lower, upper);
	}
		
	/* (non-Javadoc)
	 * @see test.type.Numeric#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#hashCode()
	 */
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#getBit()
	 */
	public boolean getBit() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#setBit(boolean)
	 */
	public void setBit(boolean value) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#getInt()
	 */
	public int getInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#setInt(int)
	 */
	public void setInt(int value) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#getReal()
	 */
	public double getReal() {
		return this.value;
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#setReal(double)
	 */
	public void setReal(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see test.type.Numeric#compareTo(test.type.Numeric)
	 */
	public int compareTo(Numeric other) {
		if (this.value == other.getReal())
			return 0;
		else 
			return (other.getReal() < value) ? 1 : -1;
	}

	/**
	 * Re-randomize the <code>Real</code> object based on the upper and lower bounds
	 */
	public void randomize() {
		this.value = (getUpperBound()-getLowerBound())*Math.random() + getLowerBound();
	}
	
	/**
	 * Return a <code>String</code> representation of the <code>Real</code> object
	 * @return A <code>String</code> representing the object instance
	 */
	public String toString() {
		return "Real[" + value + "]";
	}
}
