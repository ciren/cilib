/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import fj.F;
import fj.F2;
import fj.P;
import fj.P1;
import java.util.*;
import net.sourceforge.cilib.util.Visitor;
import net.sourceforge.cilib.math.VectorMath;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.*;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Mathematical vector implementation. This class represents a vector within
 * a predefined vector space.
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
 * favour of static factory methods.
 *
 * <p><strong>Please take note of all deprecations, it influences the usage
 * quite substantially. All deprecations will be removed in a subsequent
 * version.</strong>
 *
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

    public static Vector fill(Numeric n, int size) {
    	Numeric[] a = new Numeric[size];
    	for (int i = 0; i < size; i++) {
    		a[i] = n.getClone();
    	}
    	return new Vector(a);
    }

    public static Vector fill(Number n, int size) {
    	Numeric[] a = new Numeric[size];
    	for (int i = 0; i < size; i++) {
    		a[i] = Real.valueOf(n.doubleValue());
    	}
    	return new Vector(a);
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
     * @return An array of the elements within this {@code Vector}.
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
     * @param element The instance to add to the current {@code Vector}.
     * @return {@code true} if successful, {@code false} otherwise.
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
     * @deprecated This method has been deprecated in favour of using the
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
     * Add the {@code element} to the indicated index of the current {@code Vector}.
     * @param element The instance to add to the current {@code Vector}.
	 * @param index The index where the new {@code element} must be added.
     * @return {@code true} if successful, {@code false} otherwise.
     * @deprecated Use the {@code Vector.Builder} instead.
     */
    public boolean insert(int index, Numeric element) {
        Numeric[] array = new Numeric[components.length + 1];
        System.arraycopy(components, 0, array, 0, index);
        array[index] = element;
		System.arraycopy(components, index, array, index+1, components.length-index);
        components = array;
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
     * @param o element whose presence in the {@code Vector} is to be tested.
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
     * Remove the first occurrence of the provided object.
     * @param o The object instance to remove.
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
     * @deprecated This method has been deprecated in favour of recreating
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
        return multiply(P.<Number>p(scalar));
    }

    public final Vector multiply(P1<Number> supplier) {
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = Real.valueOf(components[i].doubleValue() * supplier._1().doubleValue(), components[i].getBounds());
        }
        return new Vector(result);
    }

    /**
     * Perform element-wise multiplication between this {@code Vector} and
     * another {@code Vector}.
     * @param other The {@code Vector} to perform multiplication with.
     * @return A new {@code Vector} instance consisting of the result
     * of the element-wise multiplication.
     */
    public final Vector multiply(Vector other) {
        Vector result = this.getClone();
        for (int i = 0; i < other.size(); i++) {
            result.setReal(i, this.doubleValueOf(i) * other.doubleValueOf(i));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double norm() {
        return Math.sqrt(foldLeft(0, new F<Numeric, Double>() {
            @Override
            public Double f(Numeric x) {
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

        // If the norm() of the vector is 0.0, then we are talking about the "normal vector"
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
     * Determines if this vector is a zero vector
     *
     * @return True if the vector is a zero vector, false otherwise
     */
    public boolean isZero() {
        for (Numeric n : this) {
            if (Double.compare(n.doubleValue(), 0.0) != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares all elements in the {@code Vector} to find the greatest element.
     *
     * @return The greatest element in the {@code Vector}.
     */
    public Numeric max() {
        if (this.components.length == 0) {
            throw new UnsupportedOperationException("Cannot obtain the maximum element of an empty vector.");
        }

        double max = this.reduceLeft(new F2<Double, Double, Number>() {
            @Override
            public Number f(Double a, Double b) {
                return (a > b) ? a : b;
            }
        }).doubleValue();

        return Real.valueOf(max);
    }

    /**
     * Compares all elements in the {@code Vector} to find the smallest element.
     *
     * @return The smallest element in the {@code Vector}.
     */
    public Numeric min() {
        if (this.components.length == 0) {
            throw new UnsupportedOperationException("Cannot obtain the minimum element of an empty vector.");
        }

        double min = this.reduceLeft(new F2<Double, Double, Number>() {
            @Override
            public Number f(Double a, Double b) {
                return (a < b) ? a : b;
            }
        }).doubleValue();

        return Real.valueOf(min);
    }

    /**
     * Calculates a vector that is orthogonal to a number of other vectors.
     *
     * @param vs    the list of vectors.
     * @return      the orthogonal vector.
     */
    public Vector orthogonalize(Iterable<Vector> vs) {
        Vector u = copyOf(this);

        for (Vector v : vs) {
            u = u.subtract(u.project(v));
        }

        return u;
    }

    /**
     * Projects this vector onto another vector
     *
     * @param v the other vector.
     * @return  the projected vector.
     */
    public Vector project(Vector v) {
        return v.multiply(this.dot(v) / v.dot(v));
    }

    /**
     * Randomize all the elements contained within the {@code Vector}.
     * @deprecated Use {@link Vector.Builder#buildRandom()} instead.
     */
    @Deprecated
    @Override
    public void randomise() {
        for (int i = 0; i < components.length; i++) {
            this.components[i].randomise();
        }
    }

    /**
     * Return a new {@code Vector} with each element representing the sign
     * of the corresponding input elements.
     * @param input The {@code Vector} to obtain the signs from.
     * @return A new {@code Vector} instance with each element representing
     * the sign of the input element.
     */
    public static Vector sign(Vector input) {
        Vector result = input.getClone();
        for (Numeric n : input) {
            n = Real.valueOf(Math.signum(n.doubleValue()));
        }
        return result;
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
     * <p>
     * If the provided array is large enough to contain the contents
     * of this {@code Vector}, it will be appended to the end of the given
     * array and the same reference that was provided will be returned.
     * <p>
     * If the given array is, however, smaller than the {@code Vector}
     * instance, the contents of the array will be copied to a new array,
     * which will be created internally, with the {@code Vector} elements
     * appended directly after the contents of the array. The operation also
     * holds true for arrays of {@code length} 0.
     * <p>
     * {@code toArray(new Object[0])} is identical to the method call
     * {@code toArray()}.
     *
     * @param <T>
     * @param a the input vector.
     * @return  an array representation of the vector.
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
     * @return whether the operation was successful.
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
    public Vector map(F<Numeric, Numeric> function) {
        Numeric[] result = new Numeric[components.length];
        for (int i = 0, n = components.length; i < n; i++) {
            result[i] = function.f(components[i]);
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
     * {@link #map(fj.F)}
     * except that the option of an initial value can be provided.
     * @param initial The initial value for the {@code fold} operation.
     * @param function to be used in the folding operations.
     * @return a scalar vale which is the result of the fold.
     */
    public double foldLeft(double initial, F<Numeric, Double> function) {
        double acc = initial;
        for (int i = 0, n = components.length; i < n; i++) {
            acc += function.f(components[i]);
        }
        return acc;
    }

    /**
     * Reduce a collection of elements to a single scalar value, based on the
     * given function (which is used to perform the reduction).
     * @param function provided to perform the reduction.
     * @return scalar value of the reduction operation.
     */
    public Number reduceLeft(F2<Double, Double, Number> function) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("empty.reduceLeft");
        }

        boolean first = true;
        Number acc = 0.0;
        for (Numeric n : this) {
            if (first) {
                acc = n.doubleValue();
                first = false;
            } else {
                acc = function.f(acc.doubleValue(), n.doubleValue());
            }
        }
        return acc;
    }
    /**
     * Permute the elements of the {@code Vector} instance to create
     * a new {@code Vector}.
     * @return A permuted {@code Vector} instance.
     */
    public Vector permute() {
        Vector.Builder builder = Vector.newBuilder();
        Vector current = this.copyOf(this);

        while(current.size() > 0) {
            int index = Rand.nextInt(current.size());
            builder.add(current.get(index));
            current.remove(index);
        }

        return builder.build();
    }

    /**
     * Sample an element uniformly from the {@code Vector}.
     * @return A uniformly sampled {@code Numeric}.
     */
    public Numeric sample() {
        return get(Rand.nextInt(size()));
    }

    /**
     * Obtain a {@link Builder} to create a {@code Vector}.
     * @return A new {@link Builder}.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * A builder for creating {@code Vector} instances. It is especially
     * useful for creating constant instances that do not change:
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

        private List<Numeric> elements;

        private Builder() {
            this.elements = Lists.newArrayList();
        }

        public Builder repeat(int n, Numeric numeric) {
            for (int i = 0; i < n; i++) {
                elements.add(numeric.getClone());
            }
            return this;
        }

        public Builder prepend(Numeric n) {
            List<Numeric> t = Lists.newArrayList(n);
            t.addAll(elements);
            elements = t;
            return this;
        }

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

        public Builder addAll(Collection<? extends Numeric> collection) {
            elements.addAll(collection);
            return this;
        }

        /**
         * Add a range of {@code int}s to the {@code Builder}. The {@code int}s
         * are wrapped within {@link Int} instances.
         * @param start the start of the range.
         * @param end the end of the range, exclusive
         * @param step the amount that is added to each value in the range
         * @return The current {@code Builder} for chaining operations.
         */
        public Builder range(int start, int end, int step) {
            checkArgument(start < end, "Range start index must be less than the end index");
            checkArgument(step > 0, "Range step must positive");

            int index = start;
            while (index < end) {
                elements.add(Int.valueOf(index));
                index += step;
            }
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
         * @param iterable the given elements.
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
         * {@code Builder}. All elements are randomised upon {@code Vector}
         * construction.
         * @return a new {@code Vector} instance created from the
         *         {@code Builder}.
         */
        public Vector buildRandom() {
            if (elements.isEmpty()) {
                return Vector.of();
            }

            Numeric[] numerics = new Numeric[elements.size()];
            int index = 0;
            for (Numeric element : elements) {
                element.randomise();
                numerics[index++] = element;
            }
            return new Vector(numerics);
        }
    }
}
