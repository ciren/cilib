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
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequencialDimensionAllocation;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@linkplain ProblemDistributionStrategy} performs a split by assigning a
 * sequential portion of the same length to each participating
 * {@linkplain PopulationBasedAlgorithm}.
 */
public class NeuralNetworkDistributionStrategy implements ProblemDistributionStrategy {

    /**
     * Splits up the given {@link OptimisationProblem} into sub-problems, where each sub problem contains a sequential, uniform length, portion of the problem verctor, and assigns all the sub-problems to the sub population {@link Algorithm}s.
     * @param populations The list of participating {@linkplain PopulationBasedAlgorithm}s.
     * @param problem The problem that needs to be re-distributed.
     * @param context The context vector maintained by the {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    @Override
    public void performDistribution(List<PopulationBasedAlgorithm> populations,
            Problem p, Vector context) {
        Preconditions.checkArgument(populations.size() >= 2, "There should at least be two Cooperating populations in a Cooperative Algorithm");
        NNTrainingProblem problem = (NNTrainingProblem)p; // convert optimisation problem to NNTrainingProblem
        NeuralNetwork nn = problem.getNeuralNetwork();
        int numLayers = nn.getArchitecture().getNumLayers();
        //System.out.println("NumLayers: " + numLayers + ", populations.size = " + populations.size());
        Preconditions.checkArgument(numLayers - 1 == populations.size(), "The number of populations should be equal to the number of NN layers minus one");
        //System.out.println("NumWeights: " + nn.getWeights().size());
        int offset = 0;
        for(int l = 1; l < nn.getArchitecture().getNumLayers(); l++) { // go through the NN layer by layer, assigning weights to subpopulations
            int previousLayerSize = nn.getArchitecture().getLayers().get(l - 1).size();
            int thisLayerSize = nn.getArchitecture().getLayers().get(l).size(); 
            if(l != nn.getArchitecture().getNumLayers() - 1) {
                thisLayerSize -=1;
            } // subtract bias, unless we're dealing with output layers
            int subpopulationSize = previousLayerSize * thisLayerSize;
            //System.out.println("l = " + l + ", previousLayerSize = " + previousLayerSize + ", thisLayerSize = "+thisLayerSize);
            DimensionAllocation allocation = new SequencialDimensionAllocation(offset, subpopulationSize);
            populations.get(l - 1).setOptimisationProblem(new CooperativeCoevolutionProblemAdapter(problem, allocation, context));
            offset += subpopulationSize;
        }
    }
}
