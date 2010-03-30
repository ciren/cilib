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

import net.sourceforge.cilib.math.random.generator.RandomProvider;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author leo
 *
 */
public class Long implements Numeric {

    private static final long serialVersionUID = -2222077877538045288L;
    private static final Bounds DEFAULT_BOUND = new Bounds(java.lang.Long.MIN_VALUE, java.lang.Long.MAX_VALUE);
    private long value;
    private final Bounds bounds;

    /**
     * Create an {@linkplain Long} with the specified value.
     * @param value The value of the {@linkplain Long}.
     */
    public Long(long value) {
        this.value = value;
        this.bounds = DEFAULT_BOUND;
    }

    /**
     * Create an instance of {@linkplain Long} with the defined {@code Bounds}.
     * @param bounds The {@code Bounds} for the instance.
     */
    public Long(long value, Bounds bounds) {
        this.value = value;
        this.bounds = checkNotNull(bounds);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public Long(Long copy) {
        this.value = copy.value;
        this.bounds = copy.bounds;
    }

    /**
     * {@inheritDoc}
     */
    public Long getClone() {
        return new Long(this);
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

        Long otherLong = (Long) obj;
        return (this.value == otherLong.value)
                && (this.bounds.equals(otherLong.bounds));
    }

    /**
     * Return the value of the object itself. This is accordance to the manner
     * in which {@see java.lang.Long#hashCode()} operates.
     *
     * @return The value of this Long representation.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + java.lang.Long.valueOf(this.value).hashCode();
        hash = 31 * hash + this.bounds.hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean booleanValue() {
        return (this.value == 0) ? false : true;
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void valueOf(boolean value) {
//        this.value = (value) ? 1 : 0;
//    }
    public long getLong() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int intValue() {
        return (int) this.value;
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void valueOf(int value) {
//        this.value = value;
//    }
    /**
     * {@inheritDoc}
     */
    @Override
    public double doubleValue() {
        return java.lang.Long.valueOf(value).doubleValue();
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void valueOf(double value) {
//        if (Double.compare(0, value) <= 0) // value is bigger or is equal
//        {
//            this.value = Double.valueOf(Math.ceil(value)).longValue();
//        } else {
//            this.value = Double.valueOf(Math.floor(value)).longValue();
//        }
//    }
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
    public void reset() {
//        this.valueOf(0);
        this.value = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    /**
     * Get the type representation of this <tt>Long</tt> object as a string.
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
        return value;
    }
}
