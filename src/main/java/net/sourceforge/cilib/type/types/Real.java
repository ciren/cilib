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
package net.sourceforge.cilib.type.types;

import static com.google.common.base.Preconditions.checkNotNull;

import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * @author Gary Pampara
 */
public class Real implements Numeric {

    private static final long serialVersionUID = 5290504438178510485L;
    private static final Bounds DEFAULT_BOUND = new Bounds(Double.MIN_VALUE, Double.MAX_VALUE);
    private double value;
    private final Bounds bounds;

    /**
     * Create the instance with the given value.
     * @param value The value of the {@linkplain Real}.
     */
    public Real(double value) {
        this.value = value;
        this.bounds = DEFAULT_BOUND;
    }

    /**
     * Create the <code>Real</code> instance with the defined {@code Bounds}.
     * @param value The initial value.
     * @param bounds The defined {@code Bounds}.
     */
    public Real(double value, Bounds bounds) {
        this.value = value;
        this.bounds = checkNotNull(bounds);
    }

    /**
     * Copy construtor.
     * @param copy The instance to copy.
     */
    public Real(Real copy) {
        this.value = copy.value;
        this.bounds = copy.bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getClone() {
        return new Real(this);
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
    public void valueOf(boolean value) {
        this.value = value ? 1.0 : 0.0;
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
    public void valueOf(int value) {
        this.value = Integer.valueOf(value).doubleValue();
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
    public void valueOf(double value) {
        this.value = value;
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
        this.value = checkNotNull(random).nextDouble() * (getBounds().getUpperBound() - getBounds().getLowerBound()) + getBounds().getLowerBound();
    }

    /**
     * Set the value of the <tt>Real</tt> to a default value of 0.0.
     */
    @Override
    public void reset() {
        this.valueOf(0.0);
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
