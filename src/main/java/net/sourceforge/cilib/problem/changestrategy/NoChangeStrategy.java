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
 * Environmental change strategy for problems that does not allow
 * a change to occur.
 */
public class NoChangeStrategy implements ChangeStrategy {

    /**
     * Do not change the search space of the provided problem instance. Leave
     * the search space intact.
     * <p>
     * This method simply returns {@code false} and performs no actions.
     * @param problem The problem to be queried for change.
     * @return {@code false} always.
     */
    @Override
    public boolean shouldApply(Problem problem) {
        return false;
        // For Theuns: Living Laxly Like A Lazy Lounge Lizzard
    }

}
