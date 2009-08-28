/**
 * Copyright (C) 2003 - 2009
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

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Types;

/**
 * The basic definition for all {@linkplain Type} objects that are based on a list.
 *
 * @param <E> The type element.
 * @author Gary Pampara
 */
public abstract class AbstractList<E extends Type> implements StructuredType<E> {
    private static final long serialVersionUID = -7855489699409219241L;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractList<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int hashCode();

    /**
     * Get the {@linkplain Type} at the given index.
     * @param index The index to inspect to return.
     * @return The {@linkplain Type} found at <code>index</code>.
     */
    public abstract E get(int index);

    /**
     * Set the {@linkplain Type} at the index <code>index</code>.
     * @param index The index to set.
     * @param value The value to set.
     */
    public abstract void set(int index, E value);

    /**
     * Insert the provided {@linkplain Type} at the specified {@code index}.
     * @param index The index where to insert the {@linkplain Type}.
     * @param value The value to set.
     */
    public abstract void insert(int index, E value);

    /**
     * Add the provided {@linkplain Type} to the end of the current list.
     * @param value The {@linkplain Type} to add.
     */
    public void append(E value) {
        int position = Types.getDimension(this);
        insert(position, value);
    }

    /**
     * Add the provided {@linkplain AbstractList} to the end of the current list.
     * @param list The object to add.
     * @return <code>true</code> if the operation was successful, <code>false</code> otherwise.
     */
    public abstract boolean append(AbstractList<E> list);

    /**
     * Prepend the provided {@linkplain Type} to the from of this list.
     * @param value The {@linkplain Type} to prepend.
     */
    public void prepend(E value) {
        insert(0, value);
    }

    /**
     * Add the provided {@linkplain AbstractList} to the start of the current list.
     * @param list The object to add.
     * @return <code>true</code> if the operation was successful, <code>false</code> otherwise.
     */
    public abstract boolean prepend(AbstractList<E> list);

    /**
     * Create an <code>Object []</code> from this <code>Vector</code>.
     * @return an <code>Object []</code> representing the <code>Vector</code>
     */
    public abstract Object[] toArray();

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
        StringBuilder representation = new StringBuilder();
        int dimension = 1;

        String current = "";
        String previous = "";

        for (int i = 0; i < size(); i++) {
            current = Types.getRepresentation(this.get(i));
            if (current.equals(previous)) {
                dimension++;
            }
            else {    //the else part will always happen for the first element
                if (dimension > 1) {
                    representation.append("^").append(String.valueOf(dimension));
                    dimension = 1;
                }
                if (i > 0) {        //Puts a ',' before the 'current' element; only when 'current' is not the first element
                        representation.append(',');
                }
                representation.append(current);
            }
            //remember the previous element, so that we can compare
            previous = current;
        }

        //in case the last couple of elements are the same
        if (dimension > 1) {
            representation.append("^").append(String.valueOf(dimension));
            dimension = 1;
        }

        return representation.toString();
    }

    /**
     * Create a sub vector from the current {@linkplain Vector}.
     * @param fromIndex The index to start the sub-list from.
     * @param toIndex The last index to end the sub-list at.
     * @return The created sub-list instance.
     */
    public abstract AbstractList<E> subList(int fromIndex, int toIndex);

    /**
     * Create a new (cloned) <tt>Vector</tt> consisting of <tt>rhs</tt> that has been appended to
     * <tt>lhs</tt>.
     * @param <T> The type element.
     * @param lhs The <tt>Vector</tt> that will form the front part of the new (cloned)
     *        <tt>Vector</tt>.
     * @param rhs The <tt>Vector</tt> that will form the back part of the new (cloned)
     *        <tt>Vector</tt>.
     * @return A new <tt>Vector</tt> consisting of the concatenation of <tt>lhs</tt> and
     *         <tt>rhs</tt>.
     */
    public static <T extends Type> AbstractList<T> append(AbstractList<T> lhs, AbstractList<T> rhs) {
        AbstractList<T> cat = lhs.getClone();
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
    @Override
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

}
