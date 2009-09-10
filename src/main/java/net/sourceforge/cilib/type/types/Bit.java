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

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;


/**
 * Implemetantation of the <tt>Bit</tt> object. The <tt>Bit</tt> object is the
 * <tt>Type</tt> system equivalent of a bit.
 *
 * @author Gary Pampara
 */
public class Bit extends Numeric {

    private static final long serialVersionUID = 1328646735062562469L;
    private boolean state;


    /**
     * Create a <tt>Bit</tt> object with a random state value.
     */
    public Bit() {
        this.state = new MersenneTwister().nextBoolean();
        super.setBounds(BoundsFactory.create(0.0, 1.0));
    }


    /**
     * Copy-constructor. Create a <tt>Bit</tt> object with the specified state.
     * @param state The state for the <tt>Bit</tt> object to be in
     */
    public Bit(boolean state) {
        this.state = state;
        super.setBounds(BoundsFactory.create(0.0, 1.0));
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public Bit(Bit copy) {
        this.state = copy.state;
        super.setBounds(copy.getBounds());
    }


    /**
     * {@inheritDoc}
     */
    public Bit getClone() {
        return new Bit(this);
    }


    /**
     * {@inheritDoc}
     */
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        Bit otherBit = (Bit) other;
        return super.equals(other) && (this.state == otherBit.state);
    }


    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + Boolean.valueOf(this.state).hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String value) {
        setBit(value);
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
     * Return the state of the curent <tt>Bit</tt> object.
     *
     * @return The state of the current <tt>Bit</tt> object.
     */
    public boolean getBit() {
        return state;
    }


    /**
     * Set the state of of this <tt>Bit</tt> object.
     *
     * @param value The state to be set.
     */
    public void setBit(boolean value) {
        this.state = value;
    }

    /**
     * {@inheritDoc}
     */
    public void setBit(String value) {
        setBit(Boolean.parseBoolean(value));
    }


    /**
     * Get the integer representation of the current <tt>Bit</tt> object.
     *
     * @return The integer value of the current <tt>Bit</tt>. Returns 1 if the state
     *         of the <tt>Bit</tt> object is <tt>true</tt>, else returns 0.
     */
    public int getInt() {
        if (state)
            return 1;
        else return 0;
    }


    /**
     * Set the state of the current <tt>Bit</tt> object.
     *
     * If the integer value is 0, the state of <tt>false</tt> is assigned, else
     * a state of <tt>true</tt> is set.
     *
     * @param value The value to be used to set the state.
     */
    public void setInt(int value) {
        if (value == 0)
            this.state = false;
        else this.state = true;
    }

    /**
     * {@inheritDoc}
     */
    public void setInt(String value) {
        setInt(Integer.parseInt(value));
    }


    /**
     * Get the state of the <tt>Bit</tt> returned as a double value.
     *
     * @return The state of this <tt>Bit</tt> object returned as a double.
     */
    public double getReal() {
        if (state)
            return 1.0;
        else return 0.0;
    }


    /**
     * Set the state of the <tt>Bit</tt> object using <i>value</i> as input. If the
     * value of <i>value</i> is 0.0, the state of the <tt>Bit</tt> is set to
     * <tt>false</tt>, else a state value of <tt>true</tt> is set.
     *
     * @param value The double value to be used to set the state.
     */
    public void setReal(double value) {
        this.state = (Double.compare(value, 0.5) < 0) ? false : true;
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
        if (state == other.getBit())
            return 0;
        else
            return state ? 1 : -1;
    }

    /**
     * Randomly choose a new valid for the <code>Bit</code> object.
     */
    public void randomize(Random random) {
        this.state = random.nextBoolean();
    }

    /**
     * Set the <tt>Bit</tt> object to an initial value of <tt>false</tt>.
     */
    public void reset() {
        this.setBit(false);
    }


    /**
     * Return the <tt>String</tt> representation of this object's value.
     *
     * @return The <tt>String</tt> represtnation of this object's value.
     */
    public String toString() {
        return state ? "1" : "0";
    }

    /**
     * Get the type representation of this <tt>Bit</tt> object as a string.
     *
     * @return The String representation of this <tt>Type</tt> object.
     */
    public String getRepresentation() {
        return "B";
    }


    /**
     * Externalise the current object to the provided <tt>ObjectOutput</tt>.
     *
     * @param oos The provided <tt>ObjectOutput</tt>
     * @throws IOException if an error occurs.
     */
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeBoolean(state);
    }


    /**
     * Externalise the current object to the provided <tt>ObjectInput</tt>.
     *
     * @param ois The provided <tt>ObjectInput</tt>
     * @throws IOException if an error occurs.
     * @throws ClassNotFoundException if the required class is not found.
     */
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        this.state = ois.readBoolean();
    }

    /**
     * Set the bounds for the current {@code Bit} instance. This method is not a valid
     * operation and as a result throws a {@code UnsupportedOperationException}.
     * @param bounds The bounds to set.
     * @throws UnsupportedOperationException Bit instances cannot have arbitary bounds.
     */
    @Override
    public void setBounds(Bounds bounds) {
        throw new UnsupportedOperationException("Bounds may not be altered for Bit instances.");
    }

    /**
     * Set the bounds for the current {@code Bit} instance. This method is not a valid
     * operation and as a result throws a {@code UnsupportedOperationException}.
     * @param lower The lower bound value.
     * @param upper The upperbound value.
     * @throws UnsupportedOperationException Bit instances cannot have arbitary bounds.
     */
    @Override
    public void setBounds(double lower, double upper) {
        throw new UnsupportedOperationException("Bounds may not be altered for Bit instances.");
    }

}
