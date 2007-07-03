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
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.VectorMath;

/**
 * @author Gary Pampara
 */
public abstract class Vector implements Graph<Type>, VectorMath {
	public abstract Vector clone();

	public boolean equals(Object other) {
		if (other instanceof Vector) {
			Vector tmp = (Vector) other;
			int dimension = this.getDimension();
			if (dimension != tmp.getDimension()) {
				return false;
			}
			for (int i = 0; i < dimension; ++i) {
				if (!this.get(i).equals(tmp.get(i))) {
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

	public abstract Type get(int index);

	public abstract void set(int index, Type value);

	public abstract void insert(int index, Type value);

	public void append(Type value) {
		insert(getDimension(), value);
	}

	public abstract boolean append(Vector vector);

	public void prepend(Type value) {
		insert(0, value);
	}

	public abstract boolean prepend(Vector vector);

	public abstract boolean getBit(int index);

	public abstract void setBit(int index, boolean value);

	public abstract int getInt(int index);

	public abstract void setInt(int index, int value);

	public abstract double getReal(int index);

	public abstract void setReal(int index, double value);

	/**
	 * Create an <code>Object []</code> from this <code>Vector</code>
	 * @return an <code>Object []</code> representing the <code>Vector</code>
	 */
	public abstract Object[] toArray();

	protected abstract Type getType(int index);

	public String getRepresentation() {
		return this.toString();
	}

	public abstract Vector subVector(int fromIndex, int toIndex);

	/**
	 * Create a new (cloned) <tt>Vector</tt> consisting of <tt>rhs</tt> that has been appended to
	 * <tt>lhs</tt>.
	 * @param lhs The <tt>Vector</tt> that will form the front part of the new (cloned)
	 *        <tt>Vector</tt>.
	 * @param rhs The <tt>Vector</tt> that will form the back part of the new (cloned)
	 *        <tt>Vector</tt>.
	 * @return A new <tt>Vector</tt> consisting of the concatenation of <tt>lhs</tt> and
	 *         <tt>rhs</tt>.
	 */
	public static Vector append(Vector lhs, Vector rhs) {
		Vector cat = lhs.clone();
		cat.append(rhs.clone());
		return cat;
	}

	/**
	 * Create a new (cloned) <tt>Vector</tt> consisting of <tt>rhs</tt> that has been prepended
	 * to <tt>lhs</tt>.
	 * @param lhs The <tt>Vector</tt> that will form the back part of the new (cloned)
	 *        <tt>Vector</tt>.
	 * @param rhs The <tt>Vector</tt> that will form the front part of the new (cloned)
	 *        <tt>Vector</tt>.
	 * @return A new <tt>Vector</tt> consisting of the concatenation of <tt>rhs</tt> and
	 *         <tt>lhs</tt>.
	 */
	public static Vector prepend(Vector lhs, Vector rhs) {
		Vector cat = rhs.clone();
		cat.append(lhs.clone());
		return cat;
	}

	/**
	 * Generate a <tt>String</tt> representation of this <tt>Vector</tt> using the provided
	 * first, last and delimiter characters.
	 * <p>
	 * Example Input: Assume <tt>first</tt> = <code>'['</code>, <tt>last</tt> =
	 * <code>']'</code>, <tt>delimiter</tt> = <code>','</code> and elements of the
	 * <tt>Vector</tt> = {1,2,3,4,5} <br>
	 * Example Output: <code>[1,2,3,4,5]</code>
	 * <p>
	 * In the case where first and last characters are not desired, call the function as follows:
	 * <br>
	 * <code>toString((char)0, (char)0, ',');</code> The delimiter character may be any character
	 * including a tab <code>'\t'</code> or a newline <code>'\n'</code>.
	 * @param first The character that indicates the start of the <tt>Vector</tt>
	 *        <tt>String</tt>
	 * @param last The character that indicates the end of the <tt>Vector</tt>
	 *        <tt>String</tt>
	 * @param delimiter The character used to delimit the elements of the <tt>Vector</tt>
	 * @return a <tt>String</tt> representing this <tt>Vector</tt>
	 */
	public String toString(char first, char last, char delimiter) {
		int dimension = getDimension();
		StringBuffer tmp = new StringBuffer(10 * dimension);
		if (first != 0)
			tmp.append(first);
		if (dimension > 0) {
			tmp.append(this.get(0).toString());
			for (int i = 1; i < dimension; ++i) {
				tmp.append(delimiter);
				tmp.append(this.get(i).toString());
			}
		}
		if (last != 0)
			tmp.append(last);
		return tmp.toString();
	}

	public String toString() {
		return toString('[', ']', ',');
	}

	public String toString(char delimiter) {
		return toString('[', ']', delimiter);
	}

	public abstract void initialise(int size, Type element);

	public abstract boolean isInsideBounds();
	
}
