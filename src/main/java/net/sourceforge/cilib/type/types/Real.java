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

import net.sourceforge.cilib.math.random.generator.Random;


/**
 * @author Gary Pampara
 */
public class Real extends Numeric {
    private static final long serialVersionUID = 5290504438178510485L;
    private double value;


    /**
     * Create the instance with a random value.
     */
    public Real() {
        this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Create the instance with the given value.
     * @param value The value of the {@linkplain Real}.
     */
    public Real(double value) {
        this.value = value;
        this.setBounds(BoundsFactory.create(-Double.MAX_VALUE, Double.MAX_VALUE));
    }


    /**
     * Create the <code>Real</code> instance with the initial value which is random between <code>lower</code> and <code>upper</code>.
     * @param lower The lower boundary for the random number.
     * @param upper The upper boundary for the random number.
     */
    public Real(double lower, double upper) {
        this.setBounds(BoundsFactory.create(lower, upper));
    }

    /**
     * Copy construtor.
     * @param copy The instance to copy.
     */
    public Real(Real copy) {
        this.value = copy.value;
        this.setBounds(copy.getBounds());
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
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        Real otherReal = (Real) other;
        return Double.compare(this.value, otherReal.value) == 0 && super.equals(other);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + Double.valueOf(this.value).hashCode();
        return hash;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String value) {
        setReal(value);
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
        return Double.compare(this.value, 0.0) == 0 ? false : true;
    }


    /**
     * {@inheritDoc}
     */
    public void setBit(boolean value) {
        this.value = value ? 1.0 : 0.0;
    }

    /**
     * {@inheritDoc}
     */
    public void setBit(String value) {
        setBit(Boolean.parseBoolean(value));
    }


    /**
     * {@inheritDoc}
     */
    public int getInt() {
        return Double.valueOf(this.value).intValue();
    }


    /**
     * {@inheritDoc}
     */
    public void setInt(int value) {
        this.value = Integer.valueOf(value).doubleValue();
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
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    public void setReal(double value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    public void setReal(String value) {
        setReal(Double.parseDouble(value));
    }


    /**
     * {@inheritDoc}
     */
    public int compareTo(Numeric other) {
        final Real otherReal = (Real) other;
        return Double.compare(this.value, otherReal.value);
    }

    /**
     * Re-randomize the <code>Real</code> object based on the upper and lower bounds.
     */
    public void randomize(Random random) {
        this.value = random.nextDouble()*(getBounds().getUpperBound()-getBounds().getLowerBound()) + getBounds().getLowerBound();
    }


    /**
     * Set the value of the <tt>Real</tt> to a default value of 0.0.
     */
    public void reset() {
        this.setReal(0.0);
    }


    /**
     * Return a <code>String</code> representation of the <code>Real</code> object.
     * @return A <code>String</code> representing the object instance.
     */
    public String toString() {
        return String.valueOf(this.value);
    }

    /**
     * Get the type representation of this <tt>Real</tt> object as a string.
     *
     * @return The String representation of this <tt>Type</tt> object.
     */
    public String getRepresentation() {
        return "R(" + getBounds().getLowerBound() + "," + getBounds().getUpperBound() +")";
    }


    /**
     * Serialize the {@linkplain Real} to the provided {@linkplain ObjectOutput}.
     * @param oos The provided {@linkplain ObjectOutput}.
     * @throws IOException if an error occurs.
     */
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeDouble(this.value);
    }


    /**
     * Read the {@linkplain Real} from the provided {@linkplain ObjectInput}.
     * @param ois The provided {@linkplain ObjectInput}.
     * @throws IOException If an IO error occurs.
     * @throws ClassNotFoundException If a class cast problem occurs.
     */
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        this.value = ois.readDouble();
    }

}
