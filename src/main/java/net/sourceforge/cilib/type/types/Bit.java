/*
 * Bit.java
 * 
 * Created on Jul 18, 2005
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

import net.sourceforge.cilib.math.random.generator.MersenneTwister;


/**
 * Implemetantation of the <tt>Bit</tt> object. The <tt>Bit</tt> object is the 
 * <tt>Type</tt> system equivalent of a bit.
 * 
 * @author Gary Pampara
 */
public class Bit extends Numeric {
	
	private static final long serialVersionUID = 1328646735062562469L;
	private boolean state;
	
	
	/**
	 * Create a <tt>Bit</tt> object with a random state value.
	 */
	public Bit() {
		this.state = new MersenneTwister().nextBoolean();
		
		this.setLowerBound(0.0);
		this.setUpperBound(1.0);
	}
	
	
	/**
	 * Copy-constructor. Create a <tt>Bit</tt> object with the specified state.  
	 * @param state The state for the <tt>Bit</tt> object to be in
	 */
	public Bit(boolean state) {
		this.state = state;
		
		this.setLowerBound(0.0);
		this.setUpperBound(1.0);
	}
	
	
	public Bit(Bit copy) {
		this.state = copy.state;
		this.setLowerBound(copy.getLowerBound());
		this.setUpperBound(copy.getUpperBound());
	}
	
	
	/**
	 * Create a clone object of the current object. The clone object is created using the
	 * copy-constructor.
	 * 
	 * @return A clone of the current object.
	 */
	public Bit clone() {
		return new Bit(this);
	}

	
	/**
	 * Determine if this object is equal to another.
	 * 
	 * @return <tt>true</tt> if the objects do evaluate equal
	 *         <tt>false</tt> if the object evalues to false
	 */
	public boolean equals(Object other) {
		Bit otherBit = (Bit) other;
		
		if (getBit() == otherBit.getBit())
			return true;
		
		return false;
	}

	
	/**
	 * Return the {@see java.lang.Boolean#hashCode()} of this object
	 * 
	 * @return The <tt>hashCode</tt> of this object
	 */
	public int hashCode() {
		return Boolean.valueOf(this.state).hashCode();
	}
	
	
	@Override
	public void set(String value) {
		setBit(value);		
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
	 * Return the state of the curent <tt>Bit</tt> object.
	 * 
	 * @return The state of the current <tt>Bit</tt> object.
	 */
	public boolean getBit() {
		return state;
	}

	
	/**
	 * Set the state of of this <tt>Bit</tt> object.
	 * 
	 * @param value The state to be set.
	 */
	public void setBit(boolean value) {
		this.state = value;
	}
	
	public void setBit(String value) {
		setBit(Boolean.parseBoolean(value));
	}

	
	/**
	 * Get the integer representation of the current <tt>Bit</tt> object.
	 * 
	 * @return The integer value of the current <tt>Bit</tt>. Returns 1 if the state 
	 *         of the <tt>Bit</tt> object is <tt>true</tt>, else returns 0.  
	 */
	public int getInt() {
		if (state)
			return 1;
		else return 0;
	}

	
	/**
	 * Set the state of the current <tt>Bit</tt> object.
	 * 
	 * If the integer value is 0, the state of <tt>false</tt> is assigned, else
	 * a state of <tt>true</tt> is set.
	 * 
	 * @param value The value to be used to set the state.
	 */
	public void setInt(int value) {
		if (value == 0)
			this.state = false;
		else this.state = true;
	}
	
	public void setInt(String value) {
		setInt(Integer.parseInt(value));
	}

	
	/**
	 * Get the state of the <tt>Bit</tt> returned as a double value.
	 * 
	 * @return The state of this <tt>Bit</tt> object returned as a double.
	 */
	public double getReal() {
		if (state)
			return 1.0;
		else return 0.0;
	}

	
	/**
	 * Set the state of the <tt>Bit</tt> object using <i>value</i> as input. If the
	 * value of <i>value</i> is 0.0, the state of the <tt>Bit</tt> is set to 
	 * <tt>false</tt>, else a state value of <tt>true</tt> is set.
	 * 
	 * @param value The double value to be used to set the state.
	 */
	public void setReal(double value) {
		if (value == 0.0)
			this.state = false;
		else this.state = true;
	}
	
	public void setReal(String value) {
		setReal(Double.parseDouble(value));
	}

	
	/**
	 * Compare this <tt>Numeric</tt> object to the given <tt>Numeric</tt> object.
	 * 
	 * @return 0 if the states of the objects is the same, else return 1 if the current
	 *         state is <tt>true</tt>, else return -1
	 */
	public int compareTo(Numeric other) {
		if (state == other.getBit())
			return 0;
		else
			return state ? 1 : -1;
	}

	/**
	 * Determine if the current value for this <tt>Bit</tt>
	 * is defined within the lower and upper bounds, as specified by the domain of the
	 * problem.
	 * 
	 * The <tt>Bit</tt> only has 2 possible values,
	 * <tt>true</tt> or <tt>false</tt>
	 * 
	 * @return <tt>true</tt> always
	 */
/*	public boolean isInsideBounds() {
		return true;
	}
*/

	/**
	 * Randomly choose a new valid for the <code>Bit</code> object.
	 */
	public void randomise() {
		this.state = new MersenneTwister().nextBoolean();
	}
	
	/**
	 * Set the <tt>Bit</tt> object to an initial value of <tt>false</tt>.
	 */
	public void reset() {
		this.setBit(false);
	}
	
	
	/**
	 * Return the <tt>String</tt> representation of this object's value.
	 * 
	 * @return The <tt>String</tt> represtnation of this object's value.
	 */
	public String toString() {
		return state ? "1" : "0";		
	}

	/**
	 * Get the type representation of this <tt>Bit</tt> object as a string
	 * 
	 * @return The String representation of this <tt>Type</tt> object.
	 */
	public String getRepresentation() {
		return "B";
	}

	
	/**
	 * Externalise the current object to the provided <tt>ObjectOutput</tt>.
	 * 
	 * @param oos The provided <tt>ObjectOutput</tt>
	 * @throws IOException if an error occours.
	 */
	public void writeExternal(ObjectOutput oos) throws IOException {
		oos.writeBoolean(state);		
	}

	
	/**
	 * Externalise the current object to the provided <tt>ObjectInput</tt>.
	 * 
	 * @param oos The provided <tt>ObjectInput</tt>
	 * @throws IOException if an error occours.
	 */
	public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
		this.state = ois.readBoolean();		
	}

	
	/**
	 * Set the lowerBound for this <tt>Bit</tt>.
	 * 
	 * @throws RuntimeExcetption if the lowerBound value to set is not 0.0
	 */
	public void setLowerBound(double value) {
		if (value == 0.0)
			super.setLowerBound(0.0);
		else
			throw new RuntimeException("Bit cannot have it's lowerBound adjusted. Bit is defined to have a lowerBound of 0.0");
	}
	
	
	/**
	 * Set the upperBound for this <tt>Bit</tt>.
	 * 
	 * @throws RuntimeExcetption if the upperBound value to set is not 1.0
	 */
	public void setUpperBound(double value) {
		if (value == 1.0) 
			super.setUpperBound(1.0);
		else
			throw new RuntimeException("Bit cannot have it's upperBound adjusted. Bit is defined to have a upperBound of 1.0");
	}
}
