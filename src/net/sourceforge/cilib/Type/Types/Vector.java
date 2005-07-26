/*
 * Vector.java
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



public abstract class Vector extends Type  implements Cloneable {
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public boolean equals(Object other) {
		if (other instanceof Vector) {
			Vector tmp = (Vector) other;
			int dimension = this.getDimension();
			if (dimension != tmp.getDimension()) {
				return false;
			}
			for (int i = 0; i < dimension; ++i) {
				if (! this.get(i).equals(tmp.get(i))) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		int dimension = this.getDimension();
		int count = (dimension < 5) ? dimension : 5;
		int tmp = 0;
		for (int i = 0; i < count; ++i) {
			tmp += get(i).hashCode();
		}
		return tmp;
	}
	
	public abstract int getDimension();
	
	public abstract Type get(int index);
	public abstract void set(int index, Type value);
	
	public abstract void insert(int index, Type value);	
	public abstract void remove(int index);
	
	public  void append(Type value) {
		insert(getDimension(), value);
	}
	public  void prepend(Type value) {
		insert(0, value);
	}
	
	public abstract boolean getBit(int index);
	public abstract void setBit(int index, boolean value);

	public abstract int getInt(int index);
	public abstract void setInt(int index, int value);

	public abstract double getReal(int index);
	public abstract void setReal(int index, double value);
	
	public String toString() {
		int dimension = getDimension();
		StringBuffer tmp = new StringBuffer(10 * dimension);
		tmp.append("[");
		if (dimension > 0) {
			tmp.append(this.get(0).toString());
			for (int i = 1; i < dimension; ++i) {
				tmp.append(',');
				tmp.append(this.get(i).toString());
			}
		}
		tmp.append("]");
		return tmp.toString();
	}
		
}
