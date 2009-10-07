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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.iterator.RandomAlgorithmIterator;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@link ProblemRedistributionStrategy} works by shuffling the problems of the participating {@link PopulationBasedAlgorithm}'s. A list of
 * all the current participating algorithm's {@link CooperativeCoevolutionProblemAdapter}s is created, and then randomly re-assinged to the list
 * of algorithms.
 *
 * @author leo
 */
public class ShuffleProblemRedistributionStrategy implements
        ProblemRedistributionStrategy {
    private static final long serialVersionUID = 1852933965918949622L;

    /**
     * {@inheritDoc}
     */
    public void redistributeProblem(List<PopulationBasedAlgorithm> populations, OptimisationProblem problem, ProblemDistributionStrategy distributionStrategy, Vector context) {
        List<OptimisationProblem> problems = new ArrayList<OptimisationProblem>();
        for(PopulationBasedAlgorithm algorithm: populations){
            problems.add(algorithm.getOptimisationProblem().getClone());
        }

        RandomAlgorithmIterator<PopulationBasedAlgorithm> iterator = new RandomAlgorithmIterator<PopulationBasedAlgorithm>(populations);
        while(iterator.hasNext()){
            iterator.next().setOptimisationProblem(problems.get(0));
            problems.remove(0);
        }
    }

    @Override
    public ShuffleProblemRedistributionStrategy getClone(){
        return new ShuffleProblemRedistributionStrategy();
    }

}
