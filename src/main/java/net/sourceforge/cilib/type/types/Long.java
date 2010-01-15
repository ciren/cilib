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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || (this.getClass() != obj.getClass()))
            return false;

        Long otherLong = (Long) obj;
        return (this.value == otherLong.value) &&
            (this.bounds.equals(otherLong.bounds));
    }

    /**
     * Return the value of the object itself. This is accordance to the manner
     * in which {@see java.lang.Long#hashCode()} operates.
     *
     * @return The value of this Long representation.
     */
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
    public void set(String value) {
        setInt(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(boolean value) {
        setBit(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(double value) {
        setReal(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int value) {
        setInt(value);
    }

    /**
     * {@inheritDoc}
     */
    public boolean getBit() {
        return (this.value == 0) ? false : true;
    }

    /**
     * {@inheritDoc}
     */
    public void setBit(boolean value) {
        this.value = (value) ? 1 : 0;
    }

    /**
     * {@inheritDoc}
     */
    public void setBit(String value) {
        setBit(Boolean.parseBoolean(value));
    }

    public void setLong(long value){
        this.value = value;
    }

    public long getLong(){
        return value;
    }
    /**
     * {@inheritDoc}
     */
    public int getInt() {
        return (int)this.value;
    }

    /**
     * {@inheritDoc}
     */
    public void setInt(int value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(String value) {
        setInt(Integer.parseInt(value));
    }

    /**
     * {@inheritDoc}
     */
    public double getReal() {
        return java.lang.Long.valueOf(value).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setReal(double value) {
        if (Double.compare(0, value) <= 0) // value is bigger or is equal
            this.value = Double.valueOf(Math.ceil(value)).longValue();
        else
            this.value = Double.valueOf(Math.floor(value)).longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReal(String value) {
        setReal(Double.parseDouble(value));
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(Numeric other) {
        if (this.value == other.getInt())
            return 0;
        else
            return (other.getInt() < this.value) ? 1 : -1;
    }

    /**
     * {@inheritDoc}
     */
    public void randomize(RandomProvider random) {
        checkNotNull(random);
        double tmp = random.nextDouble()*(getBounds().getUpperBound()-getBounds().getLowerBound()) + getBounds().getLowerBound();
        this.value = Double.valueOf(tmp).intValue();
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        this.setInt(0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.valueOf(this.value);
    }

    /**
     * Get the type representation of this <tt>Long</tt> object as a string.
     *
     * @return The String representation of this <tt>Type</tt> object.
     */
    public String getRepresentation() {
        return "Z(" + Double.valueOf(getBounds().getLowerBound()).intValue() + "," +
            Double.valueOf(getBounds().getUpperBound()).intValue() +")";
    }

    /**
     * Write this {@linkplain Long} to the provided {@linkplain ObjectOutput}.
     * @param oos The {@linkplain ObjectOutput} to write on.
     * @throws IOException If an error occurs during the write operation.
     */
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeLong(this.value);
    }

    /**
     * Read off the provided {@linkplain ObjectInput}.
     * @param ois The {@linkplain ObjectInput} to read off.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class to instantiate was not found.
     */
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        this.value = ois.readInt();
    }

    @Override
    public Bounds getBounds() {
        return this.bounds;
    }

}
