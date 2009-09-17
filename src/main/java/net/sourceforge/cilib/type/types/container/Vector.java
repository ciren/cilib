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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.VectorMath;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Resetable;

/**
 *
 * @author gpampara
 */
public class Vector extends AbstractList<Numeric> implements VectorMath, Resetable {
    private static final long serialVersionUID = -4853190809813810272L;

    private List<Numeric> components;

    /**
     * Create a new empty {@code Vector}.
     */
    public Vector() {
        this.components = new ArrayList<Numeric>();
    }

    /**
     * Create a new empty {@code Vector} with {@code size} as the initial
     * capacity.
     * @param size The initial capacity.
     */
    public Vector(int size) {
        this.components = new ArrayList<Numeric>(size);
    }

    /**
     * Create a new {@code Vector} instance of the provided {@code size}, with
     * cloned copies of {@code numeric}.
     * @param size The initial size of the {@code Vector}.
     * @param numeric The {@code Numeric} to copy.
     */
    public Vector(int size, Numeric numeric) {
        this.components = new ArrayList<Numeric>(size);
        for (int i = 0; i < size; i++)
            this.components.add(numeric.getClone());
    }

    /**
     * Create a new {@code Vector} which is a copy of the provided instance.
     * @param copy The {@code Vector} to copy.
     */
    public Vector(Vector copy) {
        this.components = new ArrayList<Numeric>(copy.size());
        for (Numeric numeric : copy.components)
            this.components.add(numeric.getClone());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getClone() {
        return new Vector(this);
    }

    /**
     * Determine if this {@code Vector} is equal to the provided object. If {@code object}
     * is not the same type of object, {@code false} will be returned.
     * @param obj The object to test.
     * @return {@code true} if equal to {@code obj}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if ((obj == null) || (this.getClass() != obj.getClass()))
            return false;

        Vector otherList = (Vector) obj;
        return this.components.equals(otherList.components);
    }

    /**
     * Returns a hash code value for the {@code Vector}.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.components == null ? 0 : this.components.hashCode());
        return hash;
    }

    /**
     * Get the {@code Numeric} at the provided {@code index}.
     * @param index The index of the {@code Numeric} in the {@code Vector}.
     * @return The {@code Numeric} at index {@code index}.
     */
    @Override
    public Numeric get(int index) {
        return this.components.get(index);
    }

    /**
     * Set the {@code Numeric} type at the specified index.
     * @param index The index to set.
     * @param value The value to set.
     */
    @Override
    public void set(int index, Numeric value) {
        this.components.set(index, value);
    }

    /**
     * Insert the provided {@code Numeric} at the specified {@code index}.
     * @param index The index to insert the {@code Numeric}.
     * @param value The {@code Numeric} to insert.
     */
    @Override
    public void insert(int index, Numeric value) {
        this.components.add(index, value);
    }

    /**
     * Add the elements of {@code list} to the end of the current {@code Vector}.
     * @param list The list of elements to add.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Override
    public boolean append(AbstractList<Numeric> list) {
        for (Numeric numeric : list)
            this.components.add(numeric);

        return true;
    }

    /**
     * Add all the elements contained within {@code list} to the beginning
     * of the current {@code Vector}.
     * @param list The list to prepend to the beginning of the {@code Vector}.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Override
    public boolean prepend(AbstractList<Numeric> list) {
        for (int i = list.size()-1; i >= 0; i--)
            this.components.add(0, list.get(i));

        return true;
    }

    /**
     * Obtain an array representing this {@code Vector}.
     * @return An array of the elemetns within this {@code Vector}.
     */
    @Override
    public Object[] toArray() {
        return this.components.toArray();
    }

    /**
     * Obtain a sublist from {@code fromIndex} to {@code toIndex}.
     * @param fromIndex The starting point.
     * @param toIndex The ending point.
     * @return A {@code Vector} which is a subset of the current {@code Vector}.
     */
    @Override
    public Vector subList(int fromIndex, int toIndex) {
        // Change the final bound to be incremented by. List.subList() is exclusive on the upper bound.
        List<Numeric> list = this.components.subList(fromIndex, toIndex+1);
        Vector result = new Vector();

        for (Numeric numeric : list)
            result.add(numeric);

        return result;
    }

    /**
     * Add the {@code element} to the end of the current {@code Vector}.
     * @param element The instace to add to the current {@code Vector}.
     * @return {@code true} if successful, {@false otherwise}.
     */
    @Override
    public boolean add(Numeric element) {
        return this.components.add(element);
    }

    /**
     * Add all the elements from the provided {@code structure} to the current
     * {@code Vector}.
     * @param structure The structure containing the elements to add.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Override
    public boolean addAll(StructuredType<? extends Numeric> structure) {
        for (Numeric numeric : structure)
            this.components.add(numeric);

        return true;
    }

    /**
     * Clear the {@code Vector}. Remove all elements within the vector.
     */
    @Override
    public void clear() {
        this.components.clear();
    }

    /**
     * Returns {@code true} if the specified {@code element} is contained
     * within the current {@code Vector}.
     * @param element wlemet whose presence in the {@code Vector} is to be tested.
     * @return {@code true} if {@code element} is contained in the {@code Vector}.
     * @throws NullPointerException if the specified element is null and this
     *         list does not permit null elements. (optional).
     */
    @Override
    public boolean contains(Numeric element) {
        return this.components.contains(element);
    }

    /**
     * Return {@code true} if the {@code Vector} contains no elements.
     * @return {@code true} if the {@code Vector} contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return this.components.isEmpty();
    }

    /**
     * Obtain an iterator to traverse the {@code Vector} iteratively.
     * @return An {@code Iterator} of {@code Numeric}s.
     */
    @Override
    public Iterator<Numeric> iterator() {
        return this.components.iterator();
    }

    /**
     * Remove the first occurance of the provided object.
     * @param element The object instace to remove.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Override
    public boolean remove(Numeric element) {
        return this.components.remove(element);
    }

    /**
     * Remove the element at {@code index} from the {@code Vector}.
     * @param index index of the element to remove.
     * @return The removed {@code Numeric} instance.
     */
    @Override
    public Numeric remove(int index) {
        return this.components.remove(index);
    }

    /**
     * Remove all the objects contained within {@code structure}.
     * @param structure The structure containing objects to remove.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Override
    public boolean removeAll(StructuredType<Numeric> structure) {
        for (Numeric numeric : structure)
            this.components.remove(numeric);

        return true;
    }

    /**
     * Returns the size of the {@code Vector}.
     * @return The size of the {@code Vector}.
     */
    @Override
    public int size() {
        return this.components.size();
    }

    /**
     * Get the dimension of the {@code Vector}.
     * @return The dimension of the {@code Vector}.
     * @see Vector#size()
     */
    public int getDimension() {
        return this.components.size();
    }

    /**
     * Apply the {@code visitor} to all elements contained in this {@code Vector}.
     * @param visitor The {@code Visitor} to apply.
     */
    @Override
    public void accept(Visitor<Numeric> visitor) {
        for (Numeric numeric : this.components)
            if (!visitor.isDone())
                visitor.visit(numeric);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector plus(Vector vector) {
        if (this.components.size() != vector.size())
            throw new UnsupportedOperationException("Cannot add vectors with differing dimensions");

        final Vector result = this.getClone();
        for(int i = 0; i < result.size(); i++) {
            Numeric numeric = result.get(i);
            numeric.setReal(numeric.getReal() + vector.getReal(i));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector subtract(Vector vector) {
        if (this.components.size() != vector.size())
            throw new UnsupportedOperationException("Cannot subtract vectors with differing dimensions");

        final Vector result = this.getClone();
        for (int i = 0; i < result.size(); i++) {
            Numeric numeric = result.get(i);
            numeric.setReal(numeric.getReal() - vector.getReal(i));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector divide(double scalar) {
        if (scalar == 0.0)
            throw new ArithmeticException("Vector division by zero");

        return this.multiply(1.0 / scalar);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector multiply(double scalar) {
        final Vector result = this.getClone();
        for (Numeric numeric : result.components) {
            numeric.set(numeric.getReal() * scalar);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double norm() {
        double result = 0.0;

        for (Numeric numeric : this.components)
            result += numeric.getReal() * numeric.getReal();

        return Math.sqrt(result);
    }

    /**
     * Obtain the length of the {@code Vector}. The length is determined
     * by obtaining the {@code norm()} of the {@code Vector}.
     * @return The length of the {@code Vector}.
     */
    public final double length() {
        return this.norm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector normalize() {
        Vector local = getClone();
        double value = local.norm();

        // If the norm() of the vector is 0.0, then we are takling about the "normal vector"
        // (\vector{0}) and as a result the normal vector is it's own normal.
        return (Double.compare(value, 0.0) != 0) ? local.divide(value) : local;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double dot(Vector vector) {
        if (this.size() != vector.size()) {
            throw new ArithmeticException("Cannot perform the dot product on vectors with differing dimensions");
        }

        double result = 0.0;

        for (int i = 0; i < size(); i++) {
            result += this.getReal(i) * vector.getReal(i);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector cross(Vector vector) {
        if (this.size() != vector.size())
            throw new ArithmeticException("Cannot perform the dot product on vectors with differing dimensions");

        if (this.size() != 3) // implicitly checks that vector.size() == 3
            throw new ArithmeticException("Cannot determine the cross product on non 3-dimensional vectors.");

        Numeric n1 = this.components.get(0).getClone();
        Numeric n2 = this.components.get(1).getClone();
        Numeric n3 = this.components.get(2).getClone();

        n1.setReal(this.getReal(1)*vector.getReal(2) - this.getReal(2)*vector.getReal(1));
        n2.setReal(-(vector.getReal(2)*this.getReal(0) - vector.getReal(0)*this.getReal(2)));
        n3.setReal(this.getReal(0)*vector.getReal(1) - this.getReal(1)*vector.getReal(0));

        final Vector result = new Vector();
        result.add(n1);
        result.add(n2);
        result.add(n3);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        for (Numeric numeric : this.components)
            numeric.reset();
    }

    /**
     * Randomize all the elements contained within the {@code Vector}.
     * @param random The {@code Random} to use to randomize the {@code Vector}.
     */
    @Override
    public void randomize(Random random) {
        for (int i = 0; i < components.size(); i++) {
            this.components.get(i).randomize(random);
        }
    }

    /**
     * Set the value at {@code index} to the specified {@code value}.
     * @param index The index where the change should occur.
     * @param value The value to be set.
     */
    public void setReal(int index, double value) {
        this.components.get(index).setReal(value);
    }

    /**
     * Get the double value of the {@code Numeric} at position {@code index}.
     * @param index The index of the double value to get.
     * @return The double value of the {@code Numeric} at position {@code index}.
     */
    public double getReal(int index) {
        return this.components.get(index).getReal();
    }

    /**
     * Set the value at {@code index} to the specified {@code value}.
     * @param index The index where the change should occur.
     * @param value The value to be set.
     */
    public void setInt(int index, int value) {
        this.components.get(index).setInt(value);
    }

    /**
     * Get the int value of the {@code Numeric} at position {@code index}.
     * @param index The index of the int value to get.
     * @return The int value of the {@code Numeric} at position {@code index}.
     */
    public int getInt(int index) {
        return this.components.get(index).getInt();
    }

    /**
     * Set the value at {@code index} to the specified {@code value}.
     * @param index The index where the change should occur.
     * @param value The value to be set.
     */
    public void setBit(int index, boolean value) {
        this.components.get(index).setBit(value);
    }

    /**
     * Get the boolean value of the {@code Numeric} at position {@code index}.
     * @param index The index of the bit value to get.
     * @return The boolean value of the {@code Numeric} at position {@code index}.
     */
    public boolean getBit(int index) {
        return this.components.get(index).getBit();
    }

}
