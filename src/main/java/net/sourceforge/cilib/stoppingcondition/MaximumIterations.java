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
public class MaximumIterations implements StoppingCondition<Algorithm> {
    private static final long serialVersionUID = -6344490201879962979L;
    private int maximumIterations;

    public MaximumIterations() {
        maximumIterations = 10000;
    }

    /**
     * Create an instance, with the given number of iterations.
     * @param maximumIterations The maximum number of iterations.
     */
    public MaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        return Integer.valueOf(algorithm.getIterations()).doubleValue() / Integer.valueOf(maximumIterations).doubleValue();
    }

    @Override
    public boolean apply(Algorithm input) {
        return input.getIterations() >= maximumIterations;
    }

    public void setMaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }
}
