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

import com.google.common.base.Predicate;

import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * <p>
 * A class that implements this interface can be used to measure the progress of an algorithm.
 * Primarily, subclasses of this interface are used to determine the stopping criteria for an
 * {@link net.sourceforge.cilib.algorithm.Algorithm}. Stopping conditions are applied to algorithms
 * using {@link Algorithm#addStoppingCondition(StoppingCondition)}.
 * </p>
 * <p>
 * Stopping conditions are also useful for implementing graphical progress bars and varying inertia
 * weights etc.
 * </p>
 */
public interface StoppingCondition<T extends Algorithm> extends Predicate<T> {

    /**
     * Determines the percentage complete for the associated algorithm.
     * @returns The percentage completed as a fraction {@literal (0 <= i <= 1.0)}.
     */
    public double getPercentageCompleted(T algorithm);

}
