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

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problem.RandomDimensionAllocation;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * This {@linkplain ProblemDistributionStrategy} performs a split by assigning
 * a sequential portion of the varying length, which consists of random
 * dimensions of the problem vector, to each participating
 * {@linkplain PopulationBasedAlgorithm}. Defaults into a split of equal
 * sizes if possible. The order in which the algorithms are assigned
 * is generated randomly.
 * 
 * @author leo
 */
public class RandomGroupingDistributionStrategy implements
        ProblemDistributionStrategy {

    /**
     * Splits up the given {@link OptimisationProblem} into sub-problems, where each sub problem contains a portion of the problem, of non-uniform length, which consists of random dimensions of the problem vector, and assigns them to all the participating {@link Algorithm}s.
     * This implementation assigns a portion of length dimensionality/number of populations + 1 to dimensionality % number of populations of the participating poopulations.
     * The order in which the algorithms are assigned is generated randomly.
     * @param populations The list of participating {@linkplain PopulationBasedAlgorithm}s.
     * @param problem The problem that needs to be re-distributed.
     * @param context The context vector maintained by the {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    public void performDistribution(List<PopulationBasedAlgorithm> populations,
            OptimisationProblem problem, Vector context) {
        //need to do a completely random split depending on the number of sub populations
        MersenneTwister random = new MersenneTwister();
        Preconditions.checkArgument(populations.size() >= 2,
                "There should at least be two Cooperating populations in a Cooperative Algorithm");

        List<Integer> dimensions = new ArrayList<Integer>();
        for (int i = 0; i < problem.getDomain().getDimension(); ++i) {
            dimensions.add(i);
        }

        int dimension = problem.getDomain().getDimension() / populations.size();
        int oddDimensions = problem.getDomain().getDimension() % populations.size();
        for (int p = 0; p < populations.size(); ++p) {
            List<Integer> indexList = new ArrayList<Integer>();
            int actualDimension = dimension;
            if (p < oddDimensions) {
                actualDimension++;
            }
            List<Integer> selectedDimensions = Selection.from(dimensions).unique().random(random, actualDimension).select(Samples.all()).perform();
            for (Integer d : selectedDimensions) {
                indexList.add(d);
                dimensions.remove(d);
            }
            populations.get(p).setOptimisationProblem(new CooperativeCoevolutionProblemAdapter(problem, new RandomDimensionAllocation(indexList), context));
        }

    }
}
