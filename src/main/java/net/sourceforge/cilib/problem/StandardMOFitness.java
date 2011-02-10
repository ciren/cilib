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
package net.sourceforge.cilib.problem;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Wiehann Matthysen
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
