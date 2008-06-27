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

/**
 * Definition of the <tt>Numeric</tt> type.
 * 
 * @author Gary Pampara
 */
public abstract class Numeric extends AbstractType implements Comparable<Numeric> {
	
	private double lowerBound;
	private double upperBound;
	
	public abstract Numeric getClone();
	
	public boolean equals(Object other) {
		if (this == other)
			return true;
		
		if ((other == null) || (this.getClass() != other.getClass()))
			return false;
		
		Numeric numeric = (Numeric) other;
		return (Double.valueOf(this.lowerBound).equals(Double.valueOf(numeric.lowerBound))) &&
			(Double.valueOf(this.upperBound).equals(Double.valueOf(numeric.upperBound)));
	}
	
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + Double.valueOf(lowerBound).hashCode();
		hash = 31 * hash + Double.valueOf(upperBound).hashCode();
		return hash;
	}
	
	public abstract void set(String value);
	public abstract void set(boolean value);
	public abstract void set(int value);
	public abstract void set(double value);
	
	public abstract boolean getBit();
	public abstract void setBit(boolean value);
	public abstract void setBit(String value);
	
	public abstract int getInt();
	public abstract void setInt(int value);
	public abstract void setInt(String value);
	
	public abstract double getReal();
	public abstract void setReal(double value);
	public abstract void setReal(String value);
	
	public abstract int compareTo(Numeric other);

	public boolean isInsideBounds() {
		return true;
	}

	public int getDimension() {
		return 1;
	}

	
	/**
	 * @return Returns the lowerBound.
	 */
	public double getLowerBound() {
		return lowerBound;
	}

	/**
	 * @param lowerBound The lowerBound to set.
	 */
	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	/**
	 * @return Returns the upperBound.
	 */
	public double getUpperBound() {
		return upperBound;
	}

	/**
	 * @param upperBound The upperBound to set.
	 */
	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
}
