/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

/**
 *
 * This class implements the <code>Comparable</code> interface for a maximisation problem.
 * That is, larger fitness values have superior fitness.
 */
public class MaximisationFitness implements Fitness {
    private static final long serialVersionUID = 317110873134837946L;

    private Double value;

   /**
    * Constructs a new <code>MaximisationFitness</code> with the given fitness value.
    * @param value The actual fitness value for the problem.
    */
    public MaximisationFitness(Double value) {
        this.value = value;
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public MaximisationFitness(MaximisationFitness copy) {
        this.value = copy.value;
    }

    /**
     * {@inheritDoc}
     */
    public MaximisationFitness getClone() {
        return new MaximisationFitness(this);
    }

    /**
     * {@inheritDoc}
     */
    public Double getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public final Fitness newInstance(Double value) {
        return new MaximisationFitness(value);
    }

    /**
     * Compare the current fitness instance to the provided instance. Returns
     * a negative integer, zero and a positive integer as this object is
     * less than, equal to or greater than the specified object.
     *
     * @param other The fitness to be compared.
     * @return a negative integer, zero or a positive integer if this object is
     *         less than, equal to or greater than the specified object.
     * @see java.lang.Comparable
     */
    @Override
    public int compareTo(Fitness other) {
        if (other == InferiorFitness.instance()) {
            return 1;
        }
        return value.compareTo(other.getValue());
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || (this.getClass() != obj.getClass()))
            return false;

        Fitness other = (Fitness) obj;
        return getValue().equals(other.getValue());
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (value == null ? 0 : value.hashCode());
        return hash;
    }

}
