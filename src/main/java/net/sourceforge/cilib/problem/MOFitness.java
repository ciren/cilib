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
package net.sourceforge.cilib.problem;

import java.util.Arrays;

import net.sourceforge.cilib.type.types.Type;

/**
 * @author Edwin Peer
 *
 */
public class MOFitness implements Fitness {

    private static final long serialVersionUID = 1477723759384827131L;
    private Fitness[] fitnesses;

    public MOFitness(MOOptimisationProblem problem, Type[] solution) {
        int size = problem.size();
        fitnesses = new Fitness[size];
        for (int i = 0; i < size; ++i) {
            fitnesses[i] = problem.getFitness(i, solution[i]);
        }
    }

    public MOFitness(MOOptimisationProblem problem, Type solution) {
        int size = problem.size();
        fitnesses = new Fitness[size];
        for (int i = 0; i < size; ++i) {
            fitnesses[i] = problem.getFitness(i, solution);
        }
    }

    public MOFitness(MOFitness copy) {
        this.fitnesses = new Fitness[copy.fitnesses.length];
        for (int i = 0; i < copy.fitnesses.length; ++i) {
            this.fitnesses[i] = copy.fitnesses[i].getClone();
        }
    }

    @Override
    public MOFitness getClone() {
        return new MOFitness(this);
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

    public Fitness getFitness(int index) {
        return this.fitnesses[index];
    }

    public int getDimension() {
        return this.fitnesses.length;
    }

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
            MOFitness tmp = (MOFitness) other;

            boolean aDominateB = false;
            boolean bDominateA = false;
            boolean aMaydominateB = true;
            boolean bMaydominateA = true;

            for (int i = 0; i < this.fitnesses.length; i++) {
                int r = this.fitnesses[i].compareTo(tmp.fitnesses[i]);
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

        final MOFitness other = (MOFitness) obj;
        return Arrays.equals(fitnesses, other.fitnesses);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Fitness fitness : this.fitnesses) {
            builder.append(fitness.toString()).append(" ");
        }
        return builder.toString();
    }
}
