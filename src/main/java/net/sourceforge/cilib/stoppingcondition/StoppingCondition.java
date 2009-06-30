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

import java.io.Serializable;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * A class that implements this interface can be used to measure the progress of an algorithm.
 * Primarily, subclasses of this interface are used to determine the stopping criteria for an
 * {@link net.sourceforge.cilib.algorithm.Algorithm}. Stopping coditions are applied to algorithms
 * using {@link Algorithm#addStoppingCondition(StoppingCondition)}.
 * </p>
 * <p>
 * Stopping conditions are also useful for implementing graphical progress bars and varying inertia
 * weights etc.
 * </p>
 * @author Edwin Peer
 */
public interface StoppingCondition extends Serializable, Cloneable {
    public static byte CICLOPS_EXCLUDE_ALGORITHM = 1;

    /**
     * Determines the percentage complete for the associated algorithm.
     * @returns The percentage completed as a fraction (0 <= i <= 1.0).
     */
    public double getPercentageCompleted();

    /**
     * Determines whether the stopping condition has been satisfied (equivalent to
     * {@link #getPercentageCompleted()} == 1.0 but may be more efficient).
     * @return true when condition is satisfied, false otherwise
     */
    public boolean isCompleted();

    /**
     * Sets the algorithm that this stopping condition should be applied to. Called by
     * {@link Algorithm#addStoppingCondition(StoppingCondition)}. This ensures that any down casting
     * necessary is done only once, when the stopping codition is added to an alogorithm (as apposed
     * to after each iteration).
     * @param algorithm The applicable {@link Algorithm}.
     */
    public void setAlgorithm(Algorithm algorithm);

    /**
     * @return
     */
    public StoppingCondition getClone();
}
