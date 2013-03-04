/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.random.generator.Rand;

/**
 * Implementation of the <tt>Bit</tt> object. The <tt>Bit</tt> object is the
 * <tt>Type</tt> system equivalent of a bit.
 *
 */
public class Bit implements Numeric {

    private static final long serialVersionUID = 1328646735062562469L;
    private static final Bounds DEFAULT_BOUND = new Bounds(0, 1);
    private boolean state;
    private final Bounds bounds;

    public static Bit valueOf(boolean state) {
        return new Bit(state);
    }

    /**
     * Copy-constructor. Create a <tt>Bit</tt> object with the specified state.
     * @param state The state for the <tt>Bit</tt> object to be in
     */
    private Bit(boolean state) {
        this.state = state;
        this.bounds = DEFAULT_BOUND;
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    private Bit(Bit copy) {
        this.state = copy.state;
        this.bounds = DEFAULT_BOUND;
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
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }

        Bit otherBit = (Bit) obj;
        return (this.state == otherBit.state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.bounds.hashCode();
        hash = 31 * hash + Boolean.valueOf(this.state).hashCode();
        return hash;
    }

    /**
     * Return the state of the current <tt>Bit</tt> object.
     *
     * @return The state of the current <tt>Bit</tt> object.
     */
    @Override
    public boolean booleanValue() {
        return state;
    }

    /**
     * Get the integer representation of the current <tt>Bit</tt> object.
     *
     * @return The integer value of the current <tt>Bit</tt>. Returns 1 if the state
     *         of the <tt>Bit</tt> object is <tt>true</tt>, else returns 0.
     */
    @Override
    public int intValue() {
        if (state) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Get the state of the <tt>Bit</tt> returned as a double value.
     *
     * @return The state of this <tt>Bit</tt> object returned as a double.
     */
    @Override
    public double doubleValue() {
        if (state) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Numeric other) {
        if (state == other.booleanValue()) {
            return 0;
        } else {
            return state ? 1 : -1;
        }
    }

    /**
     * Randomly choose a new valid for the <code>Bit</code> object.
     */
    @Override
    public void randomise() {
        this.state = Rand.nextBoolean();
    }

    /**
     * Return the <tt>String</tt> representation of this object's value.
     *
     * @return The <tt>String</tt> representation of this object's value.
     */
    @Override
    public String toString() {
        return state ? "1" : "0";
    }

    /**
     * Get the type representation of this <tt>Bit</tt> object as a string.
     *
     * @return The String representation of this <tt>Type</tt> object.
     */
    @Override
    public String getRepresentation() {
        return "B";
    }

    @Override
    public Bounds getBounds() {
        return this.bounds;
    }
}
