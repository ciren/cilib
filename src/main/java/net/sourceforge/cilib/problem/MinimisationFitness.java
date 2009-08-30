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

/**
 * This class implements the <code>Comparable</code> interface for a minimisation problem.
 * That is, smaller fitness values have superior fitness.
 *
 * @author Edwin Peer
 */
public class MinimisationFitness implements Fitness {
    private static final long serialVersionUID = 8380821922737298435L;

    private Double value;

    /**
     * Constructs a new <code>MinimisationFitness</code> with the given fitness value.
     * @param value The actual fitness value for the problem.
     */
    public MinimisationFitness(Double value) {
        this.value = value;
    }

    /**
     * Create a copy of the given {@linkplain MinimisationFitness}.
     * @param copy The instance to copy.
     */
    public MinimisationFitness(MinimisationFitness copy) {
        this.value = copy.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MinimisationFitness getClone() {
        return new MinimisationFitness(this);
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
    @Override
    public final Fitness newInstance(Double value) {
        return new MinimisationFitness(value);
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

        return -value.compareTo(other.getValue());
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

    /**
     * Get the textual representation of this {@linkplain Fitness} object.
     * @return The {@linkplain String} representing this object.
     */
    public String toString() {
        return String.valueOf(value);
    }

}
