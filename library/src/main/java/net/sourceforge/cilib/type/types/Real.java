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
package net.sourceforge.cilib.type.types;

import static com.google.common.base.Preconditions.checkNotNull;

import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 */
public class Real implements Numeric {

    private static final long serialVersionUID = 5290504438178510485L;
    private static final Bounds DEFAULT_BOUND = new Bounds(-Double.MAX_VALUE, Double.MAX_VALUE);
    private double value;
    private final Bounds bounds;

    public static Real valueOf(double value) {
        return new Real(value);
    }

    public static Real valueOf(double value, Bounds bounds) {
        return new Real(value, bounds);
    }

    /**
     * Create the instance with the given value.
     * @param value The value of the {@linkplain Real}.
     */
    private Real(double value) {
        this.value = value;
        this.bounds = DEFAULT_BOUND;
    }

    /**
     * Create the <code>Real</code> instance with the defined {@code Bounds}.
     * @param value The initial value.
     * @param bounds The defined {@code Bounds}.
     */
    private Real(double value, Bounds bounds) {
        this.value = value;
        this.bounds = checkNotNull(bounds);
    }

    /**
     * Copy construtor.
     * @param copy The instance to copy.
     */
    private Real(Real copy) {
        this.value = copy.value;
        this.bounds = copy.bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getClone() {
        return Real.valueOf(value, bounds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }

        Real otherReal = (Real) obj;
        return Double.compare(this.value, otherReal.value) == 0
                && this.bounds.equals(otherReal.bounds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.bounds.hashCode();
        hash = 31 * hash + Double.valueOf(this.value).hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean booleanValue() {
        return Double.compare(this.value, 0.0) == 0 ? false : true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int intValue() {
        int result = Double.compare(value, 0.0);
        return (result >= 0)
                ? Double.valueOf(Math.ceil(value)).intValue()
                : Double.valueOf(Math.floor(value)).intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double doubleValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Numeric o) {
        final Real otherReal = (Real) o;
        return Double.compare(this.value, otherReal.value);
    }

    /**
     * Re-randomize the <code>Real</code> object based on the upper and lower bounds.
     */
    @Override
    public void randomize(RandomProvider random) {
        this.value = checkNotNull(random).nextDouble() * (bounds.getUpperBound() - bounds.getLowerBound()) + bounds.getLowerBound();
    }

    /**
     * Return a <code>String</code> representation of the <code>Real</code> object.
     * @return A <code>String</code> representing the object instance.
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    /**
     * Get the type representation of this <tt>Real</tt> object as a string.
     *
     * @return The String representation of this <tt>Type</tt> object.
     */
    @Override
    public String getRepresentation() {
        return "R" + this.bounds.toString();
    }

    @Override
    public Bounds getBounds() {
        return this.bounds;
    }
}
