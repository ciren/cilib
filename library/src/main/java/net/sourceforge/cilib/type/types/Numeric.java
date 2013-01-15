/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

/**
 * Definition of the <tt>Numeric</tt> type.
 *
 */
public interface Numeric extends Type, BoundedType, Comparable<Numeric>, Randomisable {

    /**
     * {@inheritDoc}
     */
    @Override
    Numeric getClone();

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal boolean}.
     */
    boolean booleanValue();

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal int}.
     */
    int intValue();

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal double}.
     */
    double doubleValue();

    String getRepresentation();
}
