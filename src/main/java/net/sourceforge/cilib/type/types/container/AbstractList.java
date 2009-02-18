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
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.TypeUtil;

/**
 * The basic definition for all {@linkplain Type} objects that are based on a list.
 *
 * @author Gary Pampara
 */
public abstract class AbstractList implements StructuredType<Type> {
	private static final long serialVersionUID = -7855489699409219241L;

	/**
	 * {@inheritDoc}
	 */
	public abstract AbstractList getClone();

	/**
	 * {@inheritDoc}
	 */
	public abstract boolean equals(Object obj);

	/**
	 * {@inheritDoc}
	 */
	public abstract int hashCode();

	/**
	 * Get the {@linkplain Type} at the given index.
	 * @param index The index to inspect to return.
	 * @return The {@linkplain Type} found at <code>index</code>.
	 */
	public abstract Type get(int index);

	/**
	 * Set the {@linkplain Type} at the index <code>index</code>.
	 * @param index The index to set.
	 * @param value The value to set.
	 */
	public abstract void set(int index, Type value);

	/**
	 * Insert the provided {@linkplain Type} at the specified {@code index}.
	 * @param index The index where to insert the {@linkplain Type}.
	 * @param value The value to set.
	 */
	public abstract void insert(int index, Type value);

	/**
	 * Add the provided {@linkplain Type} to the end of the current list.
	 * @param value The {@linkplain Type} to add.
	 */
	public void append(Type value) {
		int position = TypeUtil.getDimension(this);
		insert(position, value);
	}

	/**
	 * Add the provided {@linkplain AbstractList} to the end of the current list.
	 * @param vector The object to add.
	 * @return <code>true</code> if the operation was successful, <code>false</code> otherwise.
	 */
	public abstract boolean append(AbstractList vector);

	/**
	 * Prepend the provided {@linkplain Type} to the from of this list.
	 * @param value The {@linkplain Type} to prepend.
	 */
	public void prepend(Type value) {
		insert(0, value);
	}

	/**
	 * Add the provided {@linkplain AbstractList} to the start of the current list.
	 * @param vector The object to add.
	 * @return <code>true</code> if the operation was successful, <code>false</code> otherwise.
	 */
	public abstract boolean prepend(AbstractList vector);

	/**
	 * Get the {@linkplain Numeric} at the given <code>index</code>.
	 * @param index The index of the desired {@linkplain Numeric}.
	 * @return The {@linkplain Numeric} at position <code>index</code>.
	 */
	public abstract Numeric getNumeric(int index);

	/**
	 * Get the bit-value of the {@linkplain Type} at the given <code>index</code>.
	 * @param index The index of the desired {@linkplain Numeric}.
	 * @return The bit-value at position <code>index</code>.
	 */
	public abstract boolean getBit(int index);

	/**
	 * Set the value of the {@linkplain net.sourceforge.cilib.type.types.Bit} located at position <code>index</code>.
	 * @param index The index of the bit to set the value.
	 * @param value The value of the bit to set.
	 */
	public abstract void setBit(int index, boolean value);

	/**
	 * Get the value specified at {@code index} as an {@code int}.
	 * @param index The index of the value to get.
	 * @return The value at {@code index}.
	 */
	public abstract int getInt(int index);

	/**
	 * Set the value at {@code index} to {@code value}.
	 * @param index The index of the value to set.
	 * @param value The value to set.s
	 */
	public abstract void setInt(int index, int value);

	/**
	 * Get the value at {@code index} as a {@code double}.
	 * @param index The index of the value to get.
	 * @return The value as a {@code double}.
	 */
	public abstract double getReal(int index);

	/**
	 * Set the value at {@code index} to {@code value}.
	 * @param index The index of the value to set.
	 * @param value The value to set.
	 */
	public abstract void setReal(int index, double value);

	/**
	 * Create an <code>Object []</code> from this <code>Vector</code>.
	 * @return an <code>Object []</code> representing the <code>Vector</code>
	 */
	public abstract Object[] toArray();

	/**
	 * Get the {@linkplain Type} instance at the given index.
	 * @param index The position of the {@linkplain Type} to return.
	 * @return The {@linkplain Type} at index {@literal index}.
	 */
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

		for (int i = 0; i < size(); i++) {
			current = TypeUtil.getRepresentation(this.get(i));
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
//	public boolean isInsideBounds() {
//		for (Type type : this) {
//			if (!TypeUtil.isInsideBounds(type))
//				return false;
//		}
//
//		return true;
//	}

	/**
	 * Create a sub vector from the current {@linkplain Vector}.
	 * @param fromIndex The index to start the sub-vector from.
	 * @param toIndex The last index to end the sub-vector at.
	 * @return The created sub-vector instance.
	 */
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
		int dimension = size();
		StringBuilder tmp = new StringBuilder(10 * dimension);
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

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return toString('[', ']', ',');
	}

	/**
	 * Get the {@code String} representation, using the provided {@literal delimiter}. Also see
	 * @see Object#toString()
	 * @param delimiter The delimiter to use.
	 * @return The {@linkplain String} representation, using the provided delimiter.
	 */
	public String toString(char delimiter) {
		return toString('[', ']', delimiter);
	}

	/**
	 * Initialise the {@linkplain Type} to contain <code>size</code> elements all of the type
	 * <code>element</code>.
	 * @param size The required size
	 * @param element The {@linkplain Type} to use to initialise the {@linkplain AbstractList}.
	 */
	public abstract void initialise(int size, Type element);
}
