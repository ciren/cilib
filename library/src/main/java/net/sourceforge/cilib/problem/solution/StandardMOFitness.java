/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 */
public class StandardMOFitness implements MOFitness {

    private static final long serialVersionUID = 5844109676399620203L;
    private final Fitness[] fitnesses;

    StandardMOFitness(Fitness[] fitnesses) {
        this.fitnesses = fitnesses;
    }

    public StandardMOFitness(StandardMOFitness copy) {
        this.fitnesses = new Fitness[copy.fitnesses.length];
        for (int i = 0; i < copy.fitnesses.length; ++i) {
            this.fitnesses[i] = copy.fitnesses[i].getClone();
        }
    }

    @Override
    public StandardMOFitness getClone() {
        return new StandardMOFitness(this);
    }

    @Override
    public Iterator<Fitness> iterator() {
        return Iterators.forArray(this.fitnesses);
    }

    @Override
    public Double getValue() {
        // TODO: Figure out what to do here.
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Fitness newInstance(Double value) {
        // TODO: Figure out what to do here.
        throw new UnsupportedOperationException();
    }

    @Override
    public Fitness getFitness(int index) {
        return this.fitnesses[index];
    }

    @Override
    public int getDimension() {
        return this.fitnesses.length;
    }

    @Override
    public boolean dominates(MOFitness other) {
        return this.compareTo(other) > 0;
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
        } else {

            boolean aDominateB = false;
            boolean bDominateA = false;
            boolean aMaydominateB = true;
            boolean bMaydominateA = true;

            for (int i = 0; i < this.fitnesses.length; i++) {
                int r = this.fitnesses[i].compareTo(((MOFitness) other).getFitness(i));
                if (r < 0) {
                    aDominateB = true;
                    bMaydominateA = false;
                } else if (r > 0) {
                    bDominateA = true;
                    aMaydominateB = false;
                }
            }

            if (aDominateB && aMaydominateB) {
                return -1;
            } else if (bDominateA && bMaydominateA) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Arrays.hashCode(fitnesses);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        final StandardMOFitness other = (StandardMOFitness) obj;
        return Arrays.equals(fitnesses, other.fitnesses);
    }

    @Override
    public String toString() {
        return Joiner.on(" ").join(this.fitnesses);
    }
}
