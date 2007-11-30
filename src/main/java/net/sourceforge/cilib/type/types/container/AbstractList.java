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
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.math.VectorMath;
import net.sourceforge.cilib.type.types.AbstractType;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author Gary Pampara
 */
public abstract class AbstractList extends AbstractType implements Graph<Type>, VectorMath {
	public abstract AbstractList getClone();

	public boolean equals(Object other) {
		if (other instanceof AbstractList) {
			AbstractList tmp = (AbstractList) other;
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

	public abstract boolean append(AbstractList vector);

	public void prepend(Type value) {
		insert(0, value);
	}

	public abstract boolean prepend(AbstractList vector);

	public abstract Numeric getNumeric(int index);

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

	/**
	 * Get the representation of this <tt>Vector</tt> object in the form expressed by the domain notation.
	 * This method calls the <code>getRepresentation</code> method for each element in the <tt>Vector</tt>.
	 * This method is also a bit clever in the sense that it will try to detect elements with the same
	 * representation that follow on each other, i.e. to return a representation as<br/>
	 * R(-1.0, 1.0)^3<br/>
	 * instead of<br/>
	 * R(-1.0, 1.0),R(-1.0, 1.0),R(-1.0, 1.0)
	 * NOTE: This method WILL give unexpected behaviour when an element is a <tt>Vector</tt>.
	 * @return A <code>String</code> representing the <code>Type</code> in domain notation.
	 */
	public String getRepresentation() {
		String representation = "", current = "", previous = "";
		int dimension = 1;

		for (int i = 0; i < this.getDimension(); i++) {
			current = this.get(i).getRepresentation();
			if (current.equals(previous)) {
				dimension++;
			}
			else {	//the else part will always happen for the first element
				if (dimension > 1) {
					representation += "^" + String.valueOf(dimension);
					dimension = 1;
				}
				if (i > 0) {		//Puts a ',' before the 'current' element; only when 'current' is not the first element
						representation += ',';
				}
				representation += current;
			}
			//remember the previous element, so that we can compare
			previous = current;
		}

		//in case the last couple of elements are the same
		if (dimension > 1) {
			representation += "^" + String.valueOf(dimension);
			dimension = 1;
		}

		return representation;
	}

	/**
	 * Determine if all the elements of this <tt>Vector</tt> is defined within the lower and
	 * upper bounds as specified by the domain of the problem.
	 * 
	 * @return <tt>true</tt> if all elements are within the bounds, <tt>false</tt> otherwise.
	 */
	public boolean isInsideBounds() {
		for (Type type : this) {
			if (!type.isInsideBounds())
				return false;
		}

		return true;
	}

	public abstract AbstractList subVector(int fromIndex, int toIndex);

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
	public static AbstractList append(AbstractList lhs, AbstractList rhs) {
		AbstractList cat = lhs.getClone();
		cat.append(rhs.getClone());
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
	public static AbstractList prepend(AbstractList lhs, AbstractList rhs) {
		AbstractList cat = rhs.getClone();
		cat.append(lhs.getClone());
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
}
