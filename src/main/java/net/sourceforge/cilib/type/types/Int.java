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
package net.sourceforge.cilib.type.types;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import net.sourceforge.cilib.math.MathUtil;


/**
 * 
 * @author Gary Pampara
 *
 */
public class Int extends Numeric {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 271271478995857543L;
	private int value;
	
	/**
	 * Create an instance of {@linkplain Int}.
	 */
	public Int() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);		
	}

	/**
	 * Create an instance of {@linkplain Int} randomly initialised between <code>lower</code>
	 * and <code>upper</code>.
	 * @param lower The lower bound.
	 * @param upper The upper bound.
	 */
	public Int(int lower, int upper) {
		value = Double.valueOf(MathUtil.random()*(upper-lower)).intValue() + lower;
		setLowerBound(lower);
		setUpperBound(upper);
	}

	/**
	 * Create an {@linkplain Int} with the specified value.
	 * @param value The value of the {@linkplain Int}. 
	 */
	public Int(int value) {
		this.value = value;
		setLowerBound(Integer.MIN_VALUE);
		setUpperBound(Integer.MAX_VALUE);
	}
	
	/**
	 * Create a copy of the provided instance.
	 * @param copy The instance to copy. 
	 */
	public Int(Int copy) {
		this.value = copy.value;
		this.setLowerBound(copy.getLowerBound());
		this.setUpperBound(copy.getUpperBound());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Int getClone() {
		return new Int(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;
		
		if ((other == null) || (this.getClass() != other.getClass()))
			return false;
		
		Int otherInt = (Int) other;
		return super.equals(other) && (this.value == otherInt.value);
	}

	/**
	 * Return the value of the object itself. This is accordance to the manner
	 * in which {@see java.lang.Integer#hashCode()} operates.
	 * 
	 * @return The value of this Int representation.
	 */
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + super.hashCode();
		hash = 31 * hash + Integer.valueOf(this.value).hashCode();
		return hash;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(String value) {
		setInt(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(boolean value) {
		setBit(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double value) {
		setReal(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int value) {
		setInt(value);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getBit() {
		return (this.value == 0) ? false : true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBit(boolean value) {
		this.value = (value) ? 1 : 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setBit(String value) {
		setBit(Boolean.parseBoolean(value));
	}

	/**
	 * {@inheritDoc}
	 */
	public int getInt() {
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInt(int value) {
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInt(String value) {
		setInt(Integer.parseInt(value));
	}

	/**
	 * {@inheritDoc}
	 */
	public double getReal() {
		return Integer.valueOf(value).doubleValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setReal(double value) {
		this.value = Double.valueOf(value).intValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReal(String value) {
		setReal(Double.parseDouble(value));
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Numeric other) {
		if (this.value == other.getInt())
			return 0;
		else
			return (other.getInt() < this.value) ? 1 : -1;
	}
	
	/**
	 * Determine if the current value for this <tt>Int</tt> is defined within the lower and upper
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
	 * {@inheritDoc}
	 */
	public void randomise() {
		double tmp = MathUtil.random()*(getUpperBound()-getLowerBound()) + getLowerBound();
		this.value = Double.valueOf(tmp).intValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		this.setInt(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return String.valueOf(this.value);
	}

	/**
	 * Get the type representation of this <tt>Int</tt> object as a string.
	 * 
	 * @return The String representation of this <tt>Type</tt> object.
	 */
	public String getRepresentation() {
		return "Z(" + Double.valueOf(getLowerBound()).intValue() + "," +
			Double.valueOf(getUpperBound()).intValue() +")";
	}
	
	public void writeExternal(ObjectOutput oos) throws IOException {
		oos.writeInt(this.value);		
	}

	public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
		this.value = ois.readInt();		
	}
}
