/*
 * Numeric.java
 * 
 * Created on Oct 16, 2004
 * 
 * Copyright (C)  2004 - CIRG@UP 
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
package net.sourceforge.cilib.Type.Types;


/**
 * @author espeer
 */
public abstract class Numeric extends Type implements Comparable<Numeric>, Cloneable {
	
	private double lowerBound;
	private double upperBound;
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public abstract boolean equals(Object other);
	public abstract int hashCode();
	
	public abstract boolean getBit();
	public abstract void setBit(boolean value);
	
	public abstract int getInt();
	public abstract void setInt(int value);
	
	public abstract double getReal();
	public abstract void setReal(double value);
	
	public abstract int compareTo(Numeric other);

	
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
