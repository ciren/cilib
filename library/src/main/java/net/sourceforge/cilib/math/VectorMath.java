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
 */
public interface VectorMath {

    /**
     * Adding this {@code Vector} to another will result in a resultant
     * {@code Vector}.
     *
     * @param vector    the {@code Vector} to add to the current {@code Vector}.
     * @return          the resultant {@code Vector}.
     */
    Vector plus(Vector vector);

    /**
     * Subtract the provided {@code Vector} from the current {@code Vector}.
     * <p>
     * The result of the subtraction operation is a new {@code Vector} instance,
     * maintaining the immutability of the operation.
     *
     * @param vector    the {@code Vector} to subtract.
     * @return          a new {@code Vector} instance representing the result
     *                  of the operation.
     */
    Vector subtract(Vector vector);

    /**
     * Divide the elements of the current {@code Vector} by the provided {@code scalar}.
     *
     * @param scalar    the value to divide all elements within the
     *                  {@code Vector} by.
     * @return          a new {@code Vector} instance containing the result of
     *                  the operator.
     */
    Vector divide(double scalar);

    /**
     * Multiply a {@code scalar} with each component in the {@code Vector}.
     *
     * @param scalar    the scalar to multiply in.
     * @return          a new {@code Vector} instance containing the result of
     *                  the operator.
     */
    Vector multiply(double scalar);

    /**
     * Calculate the norm of this {@code Vector} object. All the elements must
     * be of type {@link net.sourceforge.cilib.type.types.Numeric}.
     *
     * @return the value of the vector norm
     */
    double norm();

    /**
     * Synonym for the {@link #norm()} of the vector.
     *
     * @return the vector length.
     */
    double length();

    /**
     * Create a unit vector from the current Vector.
     * @return the created unit vector.
     */
    Vector normalize();

    /**
     * Calculate the vector dot product of the current {@code Vector} and the
     * given {@code Vector}.
     *
     * @param vector    the given {@code Vector} object with which the vector dot
     *                  product is to be calculated.
     * @return          the dot product value.
     */
    double dot(Vector vector);

    /**
     * Get the cross-product vector based on the current {@code Vector} and the
     * given {@code Vector}. It is important to note that the cross product is only
     * valid and defined for a 3-dimensional vector, if a {@code Vector} with more
     * dimensions is provided, an ArithmeticException will be thrown.
     *
     * @param vector    the specified {@code Vector} with with the cross product
     *                  operation is to be performed.
     * @return          the orthogonal vector to the current and the specified
     *                  {@code Vector}.
     * @throws          ArithmeticException if vectors are invalid.
     */
    Vector cross(Vector vector);
}
