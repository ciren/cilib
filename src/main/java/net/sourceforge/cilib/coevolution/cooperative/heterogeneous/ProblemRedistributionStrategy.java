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
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.ContextEntity;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This interface defines a problem re-distribution strategy. This strategy is used to re-distribute a problem amongst a number of participating algorithms in a {@link HeterogeneousCooperativeAlgorithm}.
 *
 * @author leo
 *
 */
public interface ProblemRedistributionStrategy extends Cloneable {
    /**
     * Re-distribute the given problem amongst the participating algorithms.
     * @param populations A {@link List} of participating {@link PopulationBasedAlgorithm}s.
     * @param problem The {@link OptimisationProblem} that is being optimized.
     * @param distributionStrategy The {@link CooperativeCoevolutionAlgorithm}'s original {@link ProblemDistributionStrategy}, which may be used to recalculate the distribution.
     * @param context The current {@link ContextEntity} of the {@link CooperativeCoevolutionAlgorithm}.
     */
    void redistributeProblem(List<PopulationBasedAlgorithm> populations, OptimisationProblem problem, ProblemDistributionStrategy distributionStrategy, Vector context);

    /**
     * {@inheritDoc}
     */
    @Override
    ProblemRedistributionStrategy getClone();
}
