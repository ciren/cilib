/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Interface to define operations associated with Vector mathematics.
 *
 */
public interface VectorMath {

    /**
     * Adding this {@code Vector} to another will result in a resultant {@code Vector}.
     *
     * @param vector The {@code Vector} to add to the current one
     * @return The resultant {@code Vector}
     */
    Vector plus(Vector vector);

    /**
     * Subtract the provided {@code Vector} from the current {@code Vector}.
     * The result of the subtraction operation is a new {@code Vector} instance,
     * maintaining the immutability of the operation.
     * @param vector The {@code Vector} to subtract.
     * @return A new {@code Vector} instance representing the result of the operation.
     */
    Vector subtract(Vector vector);

    /**
     * Divide the elements of the current {@code Vector} by the provided {@code scalar}.
     * @param scalar The value to divide all elements within the {@code Vector} by.
     * @return A new {@code Vector} instance containing the result of the operator.
     */
    Vector divide(double scalar);

    /**
     * Multiply a {@code scalar} with each component in the {@code Vector}.
     * @param scalar The scalar to multiply in.
     * @return A new {@code Vector} instance containing the result of the operator.
     */
    Vector multiply(double scalar);

    /**
     * Calculate the norm of this <tt>Vector</tt> object. All the elements must
     * be of type {@see net.sourceforge.cilib.type.types.Numeric}.
     *
     * @return The value of the vector norm
     */
    double norm();

    /**
     * Synonym for the {@code norm()} of the vector.
     * @return The vector length.
     */
    double length();

    /**
     * Create a unit vector from the current Vector.
     * @return The created unit vector.
     */
    Vector normalize();

    /**
     * Calculate the vector dot product of the current <tt>Vector</tt> and the
     * given <tt>Vector</tt>.
     *
     * @param vector The given <tt>Vector</tt> object with which the vector dot
     *                product is to be calculated.
     * @return The dot product value.
     */
    double dot(Vector vector);

    /**
     * Get the cross-product vector based on the current <tt>Vector</tt> and the
     * given <tt>Vector</tt>. It is important to note that the cross product is only
     * valid and defined for a 3-dimensional vector, if a <tt>Vector</tt> with more
     * dimensions is provided, an ArithmeticException will be thrown.
     *
     * @param vector The specified <tt>Vector</tt> with with the cross product operation is to be performed.
     * @return The orthogonal vector to the current and the specified <tt>Vector</tt>.
     * @throws ArithmeticException if vectors are invalid.
     */
    Vector cross(Vector vector);
}
