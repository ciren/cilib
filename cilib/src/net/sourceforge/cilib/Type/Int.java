/*
 * Int.java
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
package net.sourceforge.cilib.Type;


/**
 * @author espeer
 */
public class Int extends Numeric implements Cloneable {
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public boolean equals(Object other) {
		if (other instanceof Int) {
			return value == ((Int) other).value;
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return value;
	}

	public Int() {
		value = 0;
	}
	
	public Int(int value) {
		this.value = value;
	}
	
	public boolean getBit() {
		return (value == 0) ? false : true;
	}

	public void setBit(boolean value) {
		this.value = value ? 1 : 0; 
	}	
	
	public int getInt() {
		return value;
	}

	public void setInt(int value) {
		this.value = value;
	}

	public double getReal() {
		return value;
	}

	public void setReal(double value) {
		this.value = (int) value;
	}	

	public String toString() {
		return String.valueOf(value);
	}
	
	public int compareTo(Numeric other) {
		return value - other.getInt();
	}


	private int value; 

}
