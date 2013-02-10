/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import static com.google.common.base.Preconditions.checkNotNull;
import net.sourceforge.cilib.math.random.generator.Rand;

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
     * Copy constructor.
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
     * Re-randomise the <code>Real</code> object based on the upper and lower bounds.
     */
    @Override
    public void randomise() {
        this.value = Rand.nextDouble() * (bounds.getUpperBound() - bounds.getLowerBound()) + bounds.getLowerBound();
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
