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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * @author Edwin Peer
 */
public class MaximumIterations implements StoppingCondition {
    private static final long serialVersionUID = -6344490201879962979L;

    private Algorithm algorithm;
    private int maximumIterations;

    /**
     * Creates a new instance of MaximumIterations.
     */
    public MaximumIterations() {
        maximumIterations = 10000;
    }

    /**
     * Copy constructor. Creates a copy of the given instance.
     * @param copy The instance to copy.
     */
    public MaximumIterations(MaximumIterations copy) {
        this.maximumIterations = copy.maximumIterations;
        this.algorithm = copy.algorithm;
    }

    /**
     * Create an instance, with the given number of iterations.
     * @param maximumIterations The maximum number of iterations.
     */
    public MaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }

    /**
     * {@inheritDoc}
     */
    public MaximumIterations getClone() {
        return new MaximumIterations(this);
    }

    /**
     * Get the maximum iteration count.
     * @return The maximum iterations.
     */
    public int getMaximumIterations() {
        return maximumIterations;
    }

    /**
     * Set the maximum number of iterations.
     * @param maximumIterations The value to set.
     */
    public void setMaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }

    /**
     * {@inheritDoc}
     */
    public double getPercentageCompleted() {
        return ((double) algorithm.getIterations()) / ((double) maximumIterations);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCompleted() {
        return algorithm.getIterations() >= maximumIterations;
    }

    /**
     * {@inheritDoc}
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
