/*
 * BitVector.java
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

import java.util.BitSet;


/**
 * @author espeer
 *
 */
public class BitVector extends Vector implements Cloneable {
	
	public Object clone() throws CloneNotSupportedException {
		BitVector clone = (BitVector) super.clone();
		clone.bits = (BitSet) bits.clone();
		return clone;
	}
	
	public boolean equals(Object other) {
		if (other instanceof BitVector) {
				return bits.equals(((BitVector) other).bits);
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return bits.hashCode();
	}
	
	public BitVector() {
		bits = new BitSet();
		bits.set(0, true);
	}
	
	public BitVector(int dimension) {
		bits = new BitSet();
		bits.set(dimension, true);
	}
	
	public int getDimension() {
		return bits.length() - 1;
	}

	public Type get(int index) {
		return new Bit(getBit(index));
	}

	public void set(int index, Type value) {
		if (index >= getDimension()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		if (value instanceof Numeric) {
			setBit(index, ((Numeric) value).getBit());
		}
		else if (value instanceof Vector) {
			Vector vector = (Vector) value;
			int dimension = vector.getDimension();
			for (int i = getDimension(); i > index; --i) {
				bits.set(i + dimension - 1, bits.get(i));
			}
			for (int i = 0; i < dimension; ++i) {
				bits.set(i + index, vector.getBit(i));
			}
		}
		else {
			throw new IllegalArgumentException("Attempted to set non-numeric type on a bit vector");
		}
	}
	
	public void insert(int index, Type value) {
		if (index > getDimension()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		if (value instanceof Numeric) {
			boolean tmp = ((Numeric) value).getBit();
			for (int i = getDimension() + 1; i > index; ) {
				bits.set(i, bits.get(--i));
			}
			bits.set(index, tmp);
		}
		else if (value instanceof Vector) {
			Vector vector = (Vector) value;
			int dimension = vector.getDimension();
			for (int i = getDimension(); i >= index; --i) {
				bits.set(i + dimension, bits.get(i));
			}
			for (int i = 0; i < dimension; ++i) {
				bits.set(i + index, vector.getBit(i));
			}
		}
		else {
			throw new IllegalArgumentException("Attempted to insert non-numeric type into a bit vector");
		}
	}
	
	public void remove(int index) {
		int dimension = bits.length() - 1;
		for (int i = index; i < dimension; ) {
			bits.set(i, bits.get(++i));
		}
		bits.set(dimension, false);
	}

	public boolean getBit(int index) {
		if (index >= getDimension()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return bits.get(index);
	}

	public void setBit(int index, boolean value) {
		if (index >= getDimension()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		bits.set(index, value);
	}

	public int getInt(int index) {
		return getBit(index) ? 1 : 0; 
	}

	public void setInt(int index, int value) {
		setBit(index, (value == 0) ? false : true);
	}

	public double getReal(int index) {
		return getBit(index) ? 1 : 0;
	}

	public void setReal(int index, double value) {
		setBit(index, (value == 0) ? false : true);
	}

	private BitSet bits;

}
