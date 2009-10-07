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
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.PerfectSplitDistributionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@link ProblemRedistributionStrategy} simply re-calculates the problem distribution by making use of the given {@link ProblemRedistributionStrategy}.
 * This strategy would not make sense if, for example, the current {@link ProblemDistributionStrategy} is an instance of {@link PerfectSplitDistributionStrategy}, since
 * the distribution would always be the same no matter how many times it is re-calculated.
 * @author leo
 */
public class RecalculateProblemRedistributionStrategy implements
        ProblemRedistributionStrategy {

    private static final long serialVersionUID = -4213059436118061377L;

    /**
     * {@inheritDoc}
     */
    public void redistributeProblem(List<PopulationBasedAlgorithm> populations, OptimisationProblem problem, ProblemDistributionStrategy distributionStrategy, Vector context) {
        distributionStrategy.performDistribution(populations, problem, context);
    }

    /**
     * {@inheritDoc}
     */
    public RecalculateProblemRedistributionStrategy getClone() {
        return new RecalculateProblemRedistributionStrategy();
    }
}
