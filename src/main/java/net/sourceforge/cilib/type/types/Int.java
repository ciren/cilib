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

import net.sourceforge.cilib.math.random.generator.RandomProvider;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author Gary Pampara
 */
public class Int implements Numeric {

    private static final long serialVersionUID = 271271478995857543L;
    private static final Bounds DEFAULT_BOUND = new Bounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
    private int value;
    private final Bounds bounds;

    public static Int valueOf(int value) {
        return new Int(value);
    }

    public static Int valueOf(int value, Bounds bounds) {
        return new Int(value, bounds);
    }

    @Deprecated
    public static Int valueOf(double value, Bounds bounds) {
        int result = 0;
        if (Double.compare(0, value) <= 0) // value is bigger or is equal
        {
            result = Double.valueOf(Math.ceil(value)).intValue();
        } else {
            result = Double.valueOf(Math.floor(value)).intValue();
        }

        return new Int(result, bounds);
    }

    /**
     * Create an {@linkplain Int} with the specified value.
     * @param value The value of the {@linkplain Int}.
     */
    private Int(int value) {
        this.value = value;
        this.bounds = DEFAULT_BOUND;
    }

    /**
     * Create an instance of {@linkplain Int} with the defined bounds.
     * @param bounds The defined {@code Bounds}.
     */
    private Int(int value, Bounds bounds) {
        this.value = value;
        this.bounds = checkNotNull(bounds);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    private Int(Int copy) {
        this.value = copy.value;
        this.bounds = copy.bounds;
    }

    /**
     * {@inheritDoc}
     */
    public Int getClone() {
        return new Int(this);
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

        Int otherInt = (Int) obj;
        return (this.value == otherInt.value) && this.bounds.equals(otherInt.bounds);
    }

    /**
     * Return the value of the object itself. This is accordance to the manner
     * in which {@see java.lang.Integer#hashCode()} operates.
     *
     * @return The value of this Int representation.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.bounds.hashCode();
        hash = 31 * hash + Integer.valueOf(this.value).hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean booleanValue() {
        return (this.value == 0) ? false : true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int intValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double doubleValue() {
        return Integer.valueOf(value).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Numeric other) {
        if (this.value == other.intValue()) {
            return 0;
        } else {
            return (other.intValue() < this.value) ? 1 : -1;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void randomize(RandomProvider random) {
        checkNotNull(random);
        double tmp = random.nextDouble() * (getBounds().getUpperBound() - getBounds().getLowerBound()) + getBounds().getLowerBound();
        this.value = Double.valueOf(tmp).intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    /**
     * Get the type representation of this <tt>Int</tt> object as a string.
     *
     * @return The String representation of this <tt>Type</tt> object.
     */
    @Override
    public String getRepresentation() {
        return "Z(" + Double.valueOf(getBounds().getLowerBound()).intValue() + ","
                + Double.valueOf(getBounds().getUpperBound()).intValue() + ")";
    }

    @Override
    public Bounds getBounds() {
        return this.bounds;
    }

    @Override
    public long longValue() {
        return (long) value;
    }
}
