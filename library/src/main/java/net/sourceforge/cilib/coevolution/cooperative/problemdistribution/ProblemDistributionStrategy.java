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
package net.sourceforge.cilib.coevolution.cooperative.problemdistribution;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The interface used to distribute the dimensions in a given {@link OptimisationProblem}
 * into sub-problems for a {@linkplain CooperativeCoevolutionAlgorithm}.
 */
public interface ProblemDistributionStrategy {
    /**
     * Splits up the given {@link OptimisationProblem} into sub-problems, where each sub problem
     * contains a portion of the problem vector, and assigns them to all the participating {@link Algorithm}s.
     * @param populations The list of participating {@linkplain PopulationBasedAlgorithm}s.
     * @param problem The problem that needs to be re-distributed.
     * @param context The context vector maintained by the {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    void performDistribution(List<PopulationBasedAlgorithm> populations, Problem problem, Vector context);
}
