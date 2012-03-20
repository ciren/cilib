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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.MultistartOptimisationAlgorithm;

/**
 * The maximum number of allowed restarts.
 */
public class MaximumRestarts implements StoppingCondition<MultistartOptimisationAlgorithm> {
    private static final long serialVersionUID = 8888789427315067855L;

    private int maximumRestarts;

    /** Creates a new instance of MaximumRestarts. */
    public MaximumRestarts() {
        maximumRestarts = 10;
    }

    public MaximumRestarts(int maximumRestarts) {
        this.maximumRestarts = maximumRestarts;
    }

    public int getRestarts() {
        return maximumRestarts;
    }

    public void setRestarts(int maximumRestarts) {
        this.maximumRestarts = maximumRestarts;
    }

    @Override
    public double getPercentageCompleted(MultistartOptimisationAlgorithm algorithm) {
        return ((double) algorithm.getRestarts()) / ((double) maximumRestarts + 1);
    }

    @Override
    public boolean apply(MultistartOptimisationAlgorithm input) {
        return input.getRestarts() > this.maximumRestarts;
    }
}
