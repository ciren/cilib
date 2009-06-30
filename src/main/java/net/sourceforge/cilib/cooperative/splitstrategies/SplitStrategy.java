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
package net.sourceforge.cilib.cooperative.splitstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * The interface used to split up a given {@link OptimisationProblem} into sub-problems.
 * @author Theuns Cloete
 */
public interface SplitStrategy {
    /**
     * Splits up the given {@link OptimisationProblem} into sub-problems and assigns all the sub-problems to {@link Algorithm}s in the population.
     * @param problem The {@link OptimisationProblem} that will be split up.
     * @param context The global entity context.
     * @param populations The {@link Algorithm}s participating in the {@link SplitCooperativeAlgorithm}.
     */
    public void split(OptimisationProblem problem, CooperativeEntity context, List<PopulationBasedAlgorithm> populations);
}
