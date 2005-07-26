/*
 * Bit.java
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
public class OldBit extends OldNumeric implements Cloneable {
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public boolean equals(Object other) {
		if (other instanceof OldBit) {
			return value == ((OldBit) other).value;
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return value ? 1 : 0;
	}
	
	public OldBit() {
		value = false;
	}
	
	public OldBit(boolean value) {
		this.value = value;
	}
	
	public boolean getBit() {
		return value;
	}

	public void setBit(boolean value) {
		this.value = value; 
	}	
	
	public int getInt() {
		return value ? 1 : 0;
	}

	public void setInt(int value) {
		this.value = (value == 0) ? false : true;
	}

	public double getReal() {
		return value ? 1 : 0;
	}

	public void setReal(double value) {
		this.value = (value == 0) ? false : true;
	}
		
	public String toString() {
		return value ? "1" : "0";
	}
	
	public int compareTo(OldNumeric other) {
		if (value ==  other.getBit()) {
			return 0;
		}
		else {
			return value ? 1 : -1;
		}
	}

	private boolean value;
}
