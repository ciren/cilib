/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.type.types.container;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
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
import net.sourceforge.cilib.util.Sequence;

/**
 * Mathematical vector implementation. This class represents a vector within
 * a predefiend vector space.
 *
 * <p>The intention of the class is that instances of the {@code Vector} are
 * generally speaking immutable instances. Modifications on the current
 * vector will result in a new instance of the {@code Vector} being created
 * and returned. This maintains the mathematical constructs and additionally
 * limits the user to very obvious errors with regard to usage.
 *
 * <p>If it is required that the {@code Vector} be modified to improve
 * performance please consider providing an issue on the tracker, the idea
 * is to create a {@code MutableVector} (or something similar) that will
 * accept an instance of {@code Vector} and maintain all the modifications
 * internally, without modifying the current instance.
 *
 * <p>{@code Vector} additionally implements a functional interface.
 *
 * <p><strong>Note: Many methods have been deprecated from previous versions
 * of this class.</strong> The applied deprecations have been made to enable a
 * clearer API of usage for the user. All constructors have been deprecated in
 * favor of static factory methods.
 *
 * <p><strong>Please take note of all deprecations, it influences the usage
 * quite substantially. All deprecations will be removed in a subsequent
 * version.</strong>
 *
 * @author gpampara
 */
public class Vector implements StructuredType<Numeric>,
        VectorMath, RandomAccess {

    private static final long serialVersionUID = -4853190809813810272L;
    private Numeric[] components;

    /**
     * Returns an empty {@code Vector}.
     * <p>
     * The {@code Vector} will not have any contents.
     *
     * @return empty {@code Vector}.
     */
    public static Vector of() {
        return new Vector(new Numeric[]{});
    }

    /**
     * Create a new {@code Vector} instance, with the provided list of
     * {@code Number} instances.
     * <p>
     * All {@code Number} instances are translated to {@code Real} instances
     * using {@link Real#valueOf(double)}.
     * <p>
     * Alternatively, consider using a {@link Builder} to construct the required
     * {@code Vector}.
     *
     * @param numbers The list of elements the {@code Vector} should contain.
     * @return A new {@code Vector} instance.
     */
    public static Vector of(Number... numbers) {
        Numeric[] elements = new Numeric[numbers.length];
        int index = 0;
        for (Number number : numbers) {
            elements[index++] = Real.valueOf(number.doubleValue());
        }
        return new Vector(elements);
    }

    /**
     * Create a {@code Vector} instance that consists of the provided
     * {@code Numeric} instances.
     * @param numerics The contents of the {@code Vector}.
     * @return The new {@code Vector} instance.
     */
    public static Vector of(Numeric... numerics) {
        Numeric[] elements = new Numeric[numerics.length];
        int index = 0;
        for (Numeric numeric : numerics) {
            elements[index++] = numeric.getClone();
        }
        return new Vector(elements);
    }

    /**
     * Returns a {@code Vector} containing the given elements, in order.
     * This method iterates over the contents of the provided {@code Iterable}
     * at most once.
     *
     * @param iterable The given elements contained within an {@code Iterable}.
     * @return a new {@code Vector} instance, containing the given elements.
     */
    public static Vector copyOf(Iterable<? extends Number> iterable) {
        if (iterable instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<? extends Number> coll = (Collection<? extends Number>) iterable;
            return copyOfInternal(coll);
        } else {
            return copyOfInternal(Lists.newArrayList(iterable));
        }
    }

    /**
     * Create a copy of the provided {@code Vector} instance.
     * @param input the given {@code Vector} to copy.
     * @return a new {@code Vector} based on the contents of the given
     *         {@code Vector}.
     */
    public static Vector copyOf(Vector input) {
        return newBuilder().copyOf(input).build(); // this is a little weird :(
    }

    private static Vector copyOfInternal(Collection<? extends Number> collection) {
        int size = collection.size();
        Numeric[] array = new Numeric[size];
        int index = 0;
        for (Number n : collection) {
            array[index++] = Real.valueOf(checkNotNull(n).doubleValue());
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
     * @deprecated This method has been deprecated in favor of using the
     *             {@link Vector.Builder} instead.
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
     * @deprecated This method is no longer valid. Rather recreate the
     *             {@code Vector} instance and update the reference.
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
     * Obtain an unmodifiable iterator to traverse the {@code Vector} iteratively.
     * @return An {@code Iterator} of {@code Numeric}s.
     * @throws UnsupportedOperationException if {@code remove()} is called.
     */
    @Override
    public Iterator<Numeric> iterator() {
        return new UnmodifiableIterator<Numeric>() {

            private int index = 0;

            @Override
            public final boolean hasNext() {
                return index < components.length;
            }

            @Override
            public final Numeric next() {
                return components[index++];
            }
        };
    }

    /**
     * Remove the first occurance of the provided object.
     * @param o The object instace to remove.
     * @return {@code true} if successful, {@code false} otherwise.
     * @deprecated This method has been deprecated. Rather recreate the {@code Vector}
     *             without this element using the {@link Vector.Builder} interface.
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
     * @deprecated This method has been deprecated in favor of recreating
     *             the {@code Vector} instance.
     * @throws UnsupportedOperationException if invoked.
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
     * @deprecated Use {@link Vector#size()} instead.
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
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = Real.valueOf(components[i].doubleValue() + vector.components[i].doubleValue(), components[i].getBounds());
        }
        return new Vector(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector subtract(Vector vector) {
        if (this.components.length != vector.size()) {
            throw new UnsupportedOperationException("Cannot subtract vectors with differing dimensions");
        }
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = Real.valueOf(components[i].doubleValue() - vector.components[i].doubleValue(), components[i].getBounds());
        }
        return new Vector(result);
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
        return multiply(Sequence.of(scalar));
    }

    public final Vector multiply(Supplier<Number> supplier) {
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = Real.valueOf(components[i].doubleValue() * supplier.get().doubleValue(), components[i].getBounds());
        }
        return new Vector(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double norm() {
        return Math.sqrt(foldLeft(0, new Function<Numeric, Double>() {

            @Override
            public Double apply(Numeric x) {
                return x.doubleValue() * x.doubleValue();
            }
        }));
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
        Vector local = copyOf(this);
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
        for (int i = 0, n = components.length; i < n; i++) {
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

        Numeric[] n = new Numeric[components.length];
        n[0] = Real.valueOf(this.doubleValueOf(1) * vector.doubleValueOf(2) - this.doubleValueOf(2) * vector.doubleValueOf(1));
        n[1] = Real.valueOf(-(vector.doubleValueOf(2) * this.doubleValueOf(0) - vector.doubleValueOf(0) * this.doubleValueOf(2)));
        n[2] = Real.valueOf(this.doubleValueOf(0) * vector.doubleValueOf(1) - this.doubleValueOf(1) * vector.doubleValueOf(0));
        return new Vector(n);
    }

    /**
     * Randomize all the elements contained within the {@code Vector}.
     * @param random The {@code Random} to use to randomize the {@code Vector}.
     * @deprecated Use {@link Vector.Builder#buildRandom()} instead.
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
        this.components[index] = Real.valueOf(value, components[index].getBounds());
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
        this.components[index] = Int.valueOf(value, components[index].getBounds());
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
     * Obtain a {@link Builder} to create a {@code Vector}.
     * @return A new {@link Builder}.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Obtain the {@code double} representation of the element at the provided index.
     * @param index position of element
     * @return {@code double} value of index within {@code Vector}.
     */
    public double doubleValueOf(int index) {
        return this.components[index].doubleValue();
    }

    /**
     * Obtain the {@code integer} representation of the element at the provided index.
     * @param index position of element
     * @return {@code integer} value of index within {@code Vector}.
     */
    public int intValueOf(int index) {
        return this.components[index].intValue();
    }

    /**
     * Obtain the {@code long} representation of the element at the provided index.
     * @param index position of element
     * @return {@code long} value of index within {@code Vector}.
     */
    public long longValueOf(int index) {
        return components[index].longValue();
    }

    /**
     * Obtain the {@code boolean} representation of the element at the provided index.
     * @param index position of element
     * @return {@code boolean} value of index within {@code Vector}.
     */
    public boolean booleanValueOf(int index) {
        return components[index].booleanValue();
    }

    /**
     * Returns the array containing all of the elements within this
     * {@code Vector}. This method transforms the elements within the
     * {@code Vector} to an array representation.
     *
     * <p>If the provided array is large enough to contain the contents
     * of this {@code Vector}, it will be appended to the end of the given
     * array and the same reference that was provided will be returned.
     *
     * <p>If the given array is, however, smaller than the {@code Vector}
     * instance, the contents of the array will be copied to a new array,
     * which will be created internally, with the {@code Vector} elements
     * appended directly after the contents of the array. The operation also
     * holds true for arrays of {@code length} 0.
     *
     * <p>{@code toArray(new Object[0])} is identical to the method call
     * {@code toArray()}.
     *
     * @param <T>
     * @param a
     * @return
     */
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < components.length) {
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(components, components.length, a.getClass());
        }
        System.arraycopy(components, 0, a, 0, components.length);
        if (a.length > components.length) {
            a[components.length] = null;
        }
        return a;
    }

    /**
     * Returns {@code true} if all the given instances are contained within
     * the current {@code Vector}.
     * @param c the given collection to test.
     * @return {@code true} if the {@code Vector} contains all the given instances,
     *         {@code false} otherwise.
     * @see #contains(java.lang.Object)
     */
    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean containsAll(Collection<?> c) {
        Iterator<?> i = c.iterator();
        while (i.hasNext()) {
            if (!contains(i.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is not supported.
     * @param c
     * @return
     * @throws UnsupportedOperationException if invoked.
     * @deprecated This method is not valid. Rather recreate the {@code Vector}.
     */
    @Deprecated
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Obtain the {@code Bounds} of the associated {@link Numeric} located at
     * index {@code index}.
     * @param index The location of the element within the {@code Vector}.
     * @return The {@link Bounds} instance associated with the {@link Numeric} at
     *         index {@code index}.
     */
    public Bounds boundsOf(int index) {
        return components[index].getBounds();
    }

    /**
     * Return the string representation of the {@code Vector}.
     * <p>
     * All returned strings will be in the format of:
     * <pre>
     * {@literal [<item>, <item>, ..., <item>]}
     * </pre>
     * @return The string representation of the current {@code Vector}.
     */
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
     * Apply the given {@code function} on each element within this
     * {@code Vector}. The result of the {@code map} is a new {@code Vector}
     * containing the result of the applied function, for each element.
     * @param function provided to perform a transform on each element.
     * @return A new {@code Vector} containing the transformed elements.
     */
    public Vector map(Function<Numeric, Numeric> function) {
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = function.apply(components[i]);
        }
        return new Vector(result);
    }

    /**
     * Filter elements, based on the result of the given {@code predicate}.
     * @param predicate to determine if an element should be included, or not.
     * @return a {@code Vector} containing the filtered elements.
     */
    public Vector filter(Predicate<Numeric> predicate) {
        List<Numeric> result = Lists.newArrayListWithCapacity(components.length);
        for (Numeric n : components) {
            if (predicate.apply(n)) {
                result.add(n);
            }
        }
        return new Vector(result.toArray(new Numeric[]{}));
    }

    /**
     * A fold is a process to reduce a collection of values into a single
     * value, based on the provided function. {@code foldLeft} is effectively
     * the same as
     * {@link #map(net.sourceforge.cilib.type.types.container.Vector.Function)}
     * except that the option of an initial value can be provided.
     * @param initial The initial value for the {@code fold} operation.
     * @param function to be used in the folding operations.
     * @return a scalar vale which is the result of the fold.
     */
    public double foldLeft(double initial, Function<Numeric, Double> function) {
        double acc = initial;
        for (int i = 0, n = components.length; i < n; i++) {
            acc += function.apply(components[i]);
        }
        return acc;
    }

    /**
     * Reduce a collection of elements to a single scalar value, based on the
     * given function (which is used to perfrom the reduction).
     * @param function provided to perfrom the reduction.
     * @return scalar value of the reduction operation.
     */
    public Number reduceLeft(BinaryFunction<Double, Double, Number> function) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("empty.reduceLeft");
        }

        boolean first = true;
        Number acc = 0.0;
        for (Numeric n : this) {
            if (first) {
                acc = n.doubleValue();
                first = false;
            }
            else {
                acc = function.apply(acc.doubleValue(), n.doubleValue());
            }
        }
        return acc;
    }

    public interface Function<F, T> {

        T apply(F x);
    }

    public interface BinaryFunction<A, B, C> {
        C apply(A a, B b);
    }

    /**
     * A builder for creating {@code Vector} instances. It is especially
     * useful for creating contsant instances that do not change:
     * <p>
     * Example:
     * <pre>{@code
     *   public static final Vector IDENTITY
     *       = Vector.newBuilder()
     *          .add(1.0)
     *          .add(0.0)
     *          .build()}
     * </pre>
     * <p>
     * Builder instances can be reused - it is safe to call {@link #build}
     * multiple times to build multiple {@code Vector}s in series. Each new
     * vector contains the one created before it.
     */
    public static class Builder {

        private List<Numeric> elements = Lists.newArrayList();

        /**
         * Add a {@code double} to the {@code Builder}. The {@code double}
         * is wrapped within a {@link Real} instance.
         * @param value element to add.
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder add(double value) {
            elements.add(Real.valueOf(value));
            return this;
        }

        /**
         * Add an {@code int} to the {@code Builder}. The {@code int}
         * is wrapped within a {@link Int} instance.
         * @param value element to add.
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder add(int value) {
            elements.add(Int.valueOf(value));
            return this;
        }

        /**
         * Add a {@code boolean} to the {@code Builder}. The {@code boolean}
         * is wrapped within a {@link Bit} instance.
         * @param value element to add.
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder add(boolean value) {
            elements.add(Bit.valueOf(value));
            return this;
        }

        /**
         * Add a {@code Numeric} to the {@code Builder}.
         * @param numeric element to add.
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder add(Numeric numeric) {
            elements.add(numeric);
            return this;
        }

        /**
         * Add a {@code double} to the {@code Builder}, given the required
         * {@code Bounds}. The {@code double} is wrapped within a {@link Real}
         * instance, together with the given {@code Bounds}.
         * @param value element to add.
         * @param bounds bounds for the element.
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder addWithin(double value, Bounds bounds) {
            elements.add(Real.valueOf(value, checkNotNull(bounds)));
            return this;
        }

        /**
         * Add an {@code int} to the {@code Builder}, given the required
         * {@code Bounds}. The {@code int} is wrapped within a {@link Int}
         * instance, together with the given {@code Bounds}.
         * @param value element to add.
         * @param bounds bounds for the element.
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder addWithin(int value, Bounds bounds) {
            elements.add(Int.valueOf(value, checkNotNull(bounds)));
            return this;
        }

        /**
         * Add all elements provided by {@code iterable} to the current
         * {@code Builder}.
         * @param iterable the given elemetns.
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder copyOf(Iterable<? extends Numeric> iterable) {
            for (Numeric n : iterable) {
                elements.add(n.getClone());
            }
            return this;
        }

        /**
         * Construct a {@code Vector} from the built up elements within the
         * {@code Builder}.
         * @return a new {@code Vector} instance created from the
         *         {@code Builder}.
         */
        public Vector build() {
            if (elements.isEmpty()) {
                return Vector.of();
            }
            return new Vector(elements.toArray(new Numeric[]{}));
        }

        /**
         * Construct a {@code Vector} from the built up elements within the
         * {@code Builder}. All elements are randomized upon {@code Vector}
         * construction.
         * @return a new {@code Vector} instance created from the
         *         {@code Builder}.
         */
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
