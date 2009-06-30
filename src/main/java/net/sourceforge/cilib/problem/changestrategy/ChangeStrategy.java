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
package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.problem.Problem;

/**
 * Interface used to determine if change operations should be applied to the current problem.
 * <p>
 * This is particularly useful in Dynamic Environments when the need to alter the
 * problem search space.
 * </p>
 * <p>
 * It is possible to classify all problem instances as being dynamic problems. Problems that
 * remain unchanged are effectively problems where the applied change is a change that
 * preserves the problem search space, thus leaving it unchanged.
 * </p>
 */
public interface ChangeStrategy {

    /**
     * Determine whether a change should be applied to the provided {@code problem} instance.
     * @param problem The problem on which a change is to be applied.
     * @return {@code true} if a change occoured, {@code false} otherwise.
     */
    public boolean shouldApply(Problem problem);

}
