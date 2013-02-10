/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

/**
 * This class is used to represent a fitness value that is always inferior.
 * <p />
 * This class is a singleton.
 *
 */
public final class InferiorFitness implements Fitness {
    private static final long serialVersionUID = -2129092436359289935L;

    private InferiorFitness() {
    }

    /**
     * Get the cloned instance of this object. Due to this object being a
     * Singleton, the same instance is returned and is not cloned.
     */
    public InferiorFitness getClone() {
        return instance;
    }

    /**
     * Always returns null. <code>InferiorFitness</code> does not have a value. The
     * most sensible value to return is Double.NaN as it is still a value, however,
     * it represents something that is not a number (effectively null). Returning
     * Double.NaN will ensure that some of the Measurements do get an value, even if
     * the value is Double.NaN
     *
     * @return Double.NaN as the value is always inferior.
     */
    public Double getValue() {
        return Double.NaN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Fitness newInstance(Double value) {
        return instance();
    }

    /**
     * Returns -1, unless other is also the <code>InferiorFitness</code> instance.
     *
     * @param other The {@code Fitness} to compare.
     * @return 0 if other is <code>InferiorFitness</code> instance, -1 otherwise.
     */
    @Override
    public int compareTo(Fitness other) {
        return (this == other) ? 0 : -1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        Fitness otherFitness = (Fitness) object;
        return getValue().equals(otherFitness.getValue());
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getValue().hashCode();
        return hash;
    }

    /**
     * Obtain a reference to the <code>InferiorFitness</code> instance.
     *
     * @return the <code>InferiorFitness</code> instance.
     */
    public static Fitness instance() {
        return instance;
    }

    private static InferiorFitness instance = new InferiorFitness();

}
