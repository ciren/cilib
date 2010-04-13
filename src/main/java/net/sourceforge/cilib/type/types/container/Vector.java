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

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.VectorMath;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

/**
 *
 * @author gpampara
 */
public class Vector implements StructuredType<Numeric>, VectorMath, RandomAccess {

    private static final long serialVersionUID = -4853190809813810272L;
    private Numeric[] components;

    /**
     * Returns an emtpty {@code Vector}.
     * @return empty {@code Vector}.
     */
    public static Vector of() {
        return new Vector(new Numeric[]{});
    }

    public static Vector of(Number... numbers) {
        Numeric[] elements = new Numeric[numbers.length];
        int index = 0;
        for (Number number : numbers) {
            elements[index++] = Real.valueOf(number.doubleValue());
        }
        return new Vector(elements);
    }

    public static Vector of(Numeric... numerics) {
        Numeric[] elements = new Numeric[numerics.length];
        int index = 0;
        for (Numeric numeric : numerics) {
            elements[index++] = numeric.getClone();
        }
        return new Vector(elements);
    }

    public static Vector copyOf(Iterable<? extends Number> iterable) {
        if (iterable instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<? extends Number> coll = (Collection<? extends Number>) iterable;
            return copyOfInternal(coll);
        } else {
            return copyOfInternal(Lists.newArrayList(iterable));
        }
    }

    public static Vector copyOf(Vector input) {
        return newBuilder().copyOf(input).build(); // this is a little weird :(
    }

    private static Vector copyOfInternal(Collection<? extends Number> collection) {
        int size = collection.size();
        Numeric[] array = new Numeric[size];
        int index = 0;
        for (Number n : collection) {
            array[index++] = Real.valueOf(n.doubleValue());
        }
        return new Vector(array);
    }

    private Vector(Numeric[] elements) {
        this.components = elements;
    }

    /**
     * Create a new empty {@code Vector}.
     * @deprecated This constructor has been deprecated in favor of the static
     * factory methods {@code of()} and {@code copyOf()}.
     */
    @Deprecated
    public Vector() {
        this.components = new Numeric[]{};
    }

    /**
     * Create a new empty {@code Vector} with {@code size} as the initial
     * capacity.
     * @param size The initial capacity.
     * @deprecated This constructor has been deprecated in order to improve the performance
     *  of the {@code Vector}.
     */
    @Deprecated
    public Vector(int size) {
        this.components = new Numeric[size];
    }

    /**
     * Create a new {@code Vector} instance of the provided {@code size}, with
     * cloned copies of {@code numeric}.
     * @param size The initial size of the {@code Vector}.
     * @param numeric The {@code Numeric} to copy.
     * @deprecated This constructor has been deprecated in favor of the {@code Vector.Builder}.
     */
    @Deprecated
    public Vector(int size, Numeric numeric) {
        this.components = new Numeric[size];
        for (int i = 0; i < size; i++) {
            this.components[i] = numeric.getClone();
        }
    }

    /**
     * Create a new {@code Vector} which is a copy of the provided instance.
     * @param copy The {@code Vector} to copy.
     * @deprecated Use {@link Vector#copyOf(java.lang.Iterable)} instead.
     */
    @Deprecated
    public Vector(Vector copy) {
        this.components = new Numeric[copy.components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            this.components[i] = copy.components[i].getClone();
        }
    }

    /**
     * {@inheritDoc}
     * @deprecated Use {@link Vector#copyOf(java.lang.Iterable)} instead.
     */
    @Deprecated
    @Override
    public Vector getClone() {
        return Vector.copyOf(this);
    }

    /**
     * Determine if this {@code Vector} is equal to the provided object. If {@code object}
     * is not the same type of object, {@code false} will be returned.
     * @param obj The object to test.
     * @return {@code true} if equal to {@code obj}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }

        Vector otherList = (Vector) obj;
        return Arrays.deepEquals(components, otherList.components);
    }

    /**
     * Returns a hash code value for the {@code Vector}.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.components == null ? 0 : Arrays.hashCode(components));
        return hash;
    }

    /**
     * Get the {@code Numeric} at the provided {@code index}.
     * @param index The index of the {@code Numeric} in the {@code Vector}.
     * @return The {@code Numeric} at index {@code index}.
     */
    public Numeric get(int index) {
        return components[index];
    }

    /**
     * Set the {@code Numeric} type at the specified index.
     * @param index The index to set.
     * @param value The value to set.
     * @deprecated
     */
    @Deprecated
    public void set(int index, Numeric value) {
        this.components[index] = value;
    }

    /**
     * Obtain an array representing this {@code Vector}.
     * @return An array of the elemetns within this {@code Vector}.
     */
    @Override
    public Object[] toArray() {
        Object[] copy = new Object[components.length];
        int index = 0;
        for (Numeric n : components) {
            copy[index++] = n.getClone();
        }
        return copy;
    }

    /**
     * Obtain a sublist from {@code fromIndex} to {@code toIndex}, where {@code toIndex} is excluded.
     * @param fromIndex The starting point.
     * @param toIndex The ending point, excluding.
     * @return A {@code Vector} which is a subset of the current {@code Vector}.
     */
    public Vector copyOfRange(final int fromIndex, final int toIndex) {
        return new Vector(Arrays.copyOfRange(components, fromIndex, toIndex));
    }

    /**
     * Add the {@code element} to the end of the current {@code Vector}.
     * @param element The instace to add to the current {@code Vector}.
     * @return {@code true} if successful, {@false otherwise}.
     * @deprecated Use the {@code Vector.Builder} instead.
     */
    @Deprecated
    @Override
    public boolean add(Numeric element) {
        Numeric[] array = new Numeric[components.length + 1];
        System.arraycopy(components, 0, array, 0, components.length);
        array[array.length - 1] = element;
        components = array;
        return true;
    }

    /**
     * Add all the elements from the provided {@code structure} to the current
     * {@code Vector}.
     * @param c The structure containing the elements to add.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Deprecated
    @Override
    public boolean addAll(Collection<? extends Numeric> c) {
        int size = components.length + c.size();
        Numeric[] array = new Numeric[size];
        System.arraycopy(components, 0, array, 0, components.length);
        int index = components.length;
        for (Numeric numeric : c) {
            array[index++] = numeric;
        }

        this.components = array;
        return true;
    }

    /**
     * Clear the {@code Vector}. Remove all elements within the vector.
     */
    @Deprecated
    @Override
    public void clear() {
        this.components = new Numeric[]{};
    }

    /**
     * Returns {@code true} if the specified {@code element} is contained
     * within the current {@code Vector}.
     * @param o wlemet whose presence in the {@code Vector} is to be tested.
     * @return {@code true} if {@code element} is contained in the {@code Vector}.
     * @throws NullPointerException if the specified element is null and this
     *         list does not permit null elements. (optional).
     */
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < components.length; i++) {
            if (o.equals(components[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return {@code true} if the {@code Vector} contains no elements.
     * @return {@code true} if the {@code Vector} contains no elements.
     */
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < components.length; i++) {
            if (components[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtain an iterator to traverse the {@code Vector} iteratively.
     * @return An {@code Iterator} of {@code Numeric}s.
     */
    @Override
    public Iterator<Numeric> iterator() {
        return new Iterator<Numeric>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < components.length;
            }

            @Override
            public Numeric next() {
                return components[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    /**
     * Remove the first occurance of the provided object.
     * @param o The object instace to remove.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Deprecated
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < components.length; i++) {
            if (components[i].equals(o)) {
                return remove(i);
            }
        }
        return false;
    }

    private boolean remove(final int index) {
        Numeric[] array = new Numeric[components.length - 1];
        int count = 0;
        for (int i = 0; i < index; i++) {
            array[count++] = components[i];
        }
        for (int i = index + 1; i < components.length; i++) {
            array[count++] = components[i];
        }
        components = array;
        return true;
    }

    /**
     * Remove all the objects contained within {@code structure}.
     * @param c The structure containing objects to remove.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    @Deprecated
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the size of the {@code Vector}.
     * @return The size of the {@code Vector}.
     */
    @Override
    public int size() {
        return this.components.length;
    }

    /**
     * Get the dimension of the {@code Vector}.
     * @return The dimension of the {@code Vector}.
     * @see Vector#size()
     * @deprecated Rather user {@link Vector#size()}
     */
    @Deprecated
    public int getDimension() {
        return this.components.length;
    }

    /**
     * Apply the {@code visitor} to all elements contained in this {@code Vector}.
     * @param visitor The {@code Visitor} to apply.
     */
    @Override
    public void accept(Visitor<Numeric> visitor) {
        for (Numeric numeric : this.components) {
            if (!visitor.isDone()) {
                visitor.visit(numeric);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector plus(Vector vector) {
        if (this.components.length != vector.size()) {
            throw new UnsupportedOperationException("Cannot add vectors with differing dimensions");
        }

        Vector.Builder resultBuilder = Vector.newBuilder();
        for (int i = 0; i < size(); i++) {
            resultBuilder.add(doubleValueOf(i) + vector.doubleValueOf(i));
        }
        return resultBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector subtract(Vector vector) {
        if (this.components.length != vector.size()) {
            throw new UnsupportedOperationException("Cannot subtract vectors with differing dimensions");
        }

        Vector.Builder resultBuilder = Vector.newBuilder();
        for (int i = 0; i < size(); i++) {
            resultBuilder.add(doubleValueOf(i) - vector.doubleValueOf(i));
        }
        return resultBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector divide(double scalar) {
        if (scalar == 0.0) {
            throw new ArithmeticException("Vector division by zero");
        }

        return this.multiply(1.0 / scalar);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector multiply(double scalar) {
        Vector.Builder resultBuilder = Vector.newBuilder();
        for (Numeric numeric : components) {
            resultBuilder.add(numeric.doubleValue() * scalar);
        }
        return resultBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double norm() {
        double result = 0.0;

        for (Numeric numeric : this.components) {
            result += numeric.doubleValue() * numeric.doubleValue();
        }

        return Math.sqrt(result);
    }

    /**
     * Obtain the length of the {@code Vector}. The length is determined
     * by obtaining the {@code norm()} of the {@code Vector}.
     * @return The length of the {@code Vector}.
     */
    @Override
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
            result += this.doubleValueOf(i) * vector.doubleValueOf(i);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector cross(Vector vector) {
        if (this.size() != vector.size()) {
            throw new ArithmeticException("Cannot perform the dot product on vectors with differing dimensions");
        }

        if (this.size() != 3) { // implicitly checks that vector.size() == 3
            throw new ArithmeticException("Cannot determine the cross product on non 3-dimensional vectors.");
        }

        final Vector.Builder resultBuilder = Vector.newBuilder();
        resultBuilder.add(this.doubleValueOf(1) * vector.doubleValueOf(2) - this.doubleValueOf(2) * vector.doubleValueOf(1));
        resultBuilder.add(-(vector.doubleValueOf(2) * this.doubleValueOf(0) - vector.doubleValueOf(0) * this.doubleValueOf(2)));
        resultBuilder.add(this.doubleValueOf(0) * vector.doubleValueOf(1) - this.doubleValueOf(1) * vector.doubleValueOf(0));
        return resultBuilder.build();
    }

    /**
     * Randomize all the elements contained within the {@code Vector}.
     * @param random The {@code Random} to use to randomize the {@code Vector}.
     * @deprecated
     */
    @Deprecated
    @Override
    public void randomize(RandomProvider random) {
        for (int i = 0; i < components.length; i++) {
            this.components[i].randomize(random);
        }
    }

    /**
     * Set the value at {@code index} to the specified {@code value}.
     * @param index The index where the change should occur.
     * @param value The value to be set.
     */
    public void setReal(int index, double value) {
        this.components[index] = Real.valueOf(value);
    }

    /**
     * Get the double value of the {@code Numeric} at position {@code index}.
     * @param index The index of the double value to get.
     * @return The double value of the {@code Numeric} at position {@code index}.
     * @deprecated Use {@link Vector#doubleValueOf(int)} instead.
     */
    @Deprecated
    public double getReal(int index) {
        return this.components[index].doubleValue();
    }

    /**
     * Set the value at {@code index} to the specified {@code value}.
     * @param index The index where the change should occur.
     * @param value The value to be set.
     */
    public void setInt(int index, int value) {
        this.components[index] = Int.valueOf(value);
    }

    /**
     * Get the int value of the {@code Numeric} at position {@code index}.
     * @param index The index of the int value to get.
     * @return The int value of the {@code Numeric} at position {@code index}.
     * @deprecated Use {@link Vector#intValueOf(int)} instead.
     */
    @Deprecated
    public int getInt(int index) {
        return this.components[index].intValue();
    }

    /**
     * Set the value at {@code index} to the specified {@code value}.
     * @param index The index where the change should occur.
     * @param value The value to be set.
     */
    public void setBit(int index, boolean value) {
        this.components[index] = Bit.valueOf(value);
    }

    /**
     * Get the boolean value of the {@code Numeric} at position {@code index}.
     * @param index The index of the bit value to get.
     * @return The boolean value of the {@code Numeric} at position {@code index}.
     * @deprecated Use {@link Vector#booleanValueOf(int)} instead.
     */
    @Deprecated
    public boolean getBit(int index) {
        return this.components[index].booleanValue();
    }

    /**
     *
     * @return
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    public double doubleValueOf(int index) {
        return this.components[index].doubleValue();
    }

    public int intValueOf(int index) {
        return this.components[index].intValue();
    }

    public long longValueOf(int index) {
        return components[index].longValue();
    }

    public boolean booleanValueOf(int index) {
        return components[index].booleanValue();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Bounds boundsOf(int i) {
        return components[i].getBounds();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int index = 0;
        if (size() >= 1) {
            builder.append(components[index++]);
        }
        for (int i = index; i < components.length; i++) {
            builder.append(",").append(components[index++]);
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     *
     */
    public static class Builder {

        private List<Numeric> elements = Lists.newArrayList();

        public Builder add(double value) {
            elements.add(Real.valueOf(value));
            return this;
        }

        public Builder add(int value) {
            elements.add(Int.valueOf(value));
            return this;
        }

        public Builder add(boolean value) {
            elements.add(Bit.valueOf(value));
            return this;
        }

        public Builder add(Numeric numeric) {
            elements.add(numeric);
            return this;
        }

        public Builder addWithin(double value, Bounds bounds) {
            elements.add(Real.valueOf(value, checkNotNull(bounds)));
            return this;
        }

        public Builder addWithin(int value, Bounds bounds) {
            elements.add(Int.valueOf(value, checkNotNull(bounds)));
            return this;
        }

        public Builder copyOf(Iterable<? extends Numeric> iterable) {
            for (Numeric n : iterable) {
                elements.add(n.getClone());
            }
            return this;
        }

        public Vector build() {
            if (elements.isEmpty()) {
                return Vector.of();
            }

            Numeric[] numerics = new Numeric[elements.size()];
            int index = 0;
            for (Numeric n : elements) {
                numerics[index++] = n;
            }
            return new Vector(numerics);
        }

        public Vector buildRandom() {
            if (elements.isEmpty()) {
                return Vector.of();
            }

            MersenneTwister random = new MersenneTwister(); // needs to come out, must be passed in
            Numeric[] numerics = new Numeric[elements.size()];
            int index = 0;
            for (Numeric element : elements) {
                element.randomize(random);
                numerics[index++] = element;
            }
            return new Vector(numerics);
        }
    }
}
