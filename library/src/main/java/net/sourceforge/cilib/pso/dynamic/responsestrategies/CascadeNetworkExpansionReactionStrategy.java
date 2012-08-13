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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.problem.NNDataTrainingProblem;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;

/**
 * This reaction strategy adds new dimensions, to the particle's search space,
 * corresponding to the new weights added to a cascade network when it is expanded
 * by one neuron. The new dimensions in the position, velocity and personal best
 * vectors are set to Double.NaN to indicate that they need to be initialised.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class CascadeNetworkExpansionReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
    public CascadeNetworkExpansionReactionStrategy() {
    }

    public CascadeNetworkExpansionReactionStrategy(CascadeNetworkExpansionReactionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public CascadeNetworkExpansionReactionStrategy<E> getClone() {
        return new CascadeNetworkExpansionReactionStrategy<E>(this);
    }

    /**
     * Adds a single neuron to the cascade network and adds the new dimensions
	 * to the particles, initialised as Double.NaN.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
    	NNDataTrainingProblem problem = (NNDataTrainingProblem)algorithm.getOptimisationProblem();
    	NeuralNetwork network = problem.getNeuralNetwork();

		//add a new neuron to the hidden layer
    	LayerConfiguration targetLayerConfiguration = network.getArchitecture().getArchitectureBuilder()
    											.getLayerConfigurations().get(1);
    	targetLayerConfiguration.setSize(targetLayerConfiguration.getSize() +1);
		network.initialize();

		//add new weights to all the particles in a manner that preserves the old weights
        List<? extends Entity> particles = algorithm.getTopology();
		int hiddenLayerSize = targetLayerConfiguration.getSize();
		int outputLayerSize = network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().get(2).getSize();
		int inputLayerSize = network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().get(0).getSize();
		if (network.getArchitecture().getArchitectureBuilder().getLayerConfigurations().get(0).isBias()) {
			inputLayerSize += 1;
		}
		for (Entity curParticle : particles) {
			DynamicParticle curDynamicParticle = (DynamicParticle)curParticle;
			Bounds bounds = curDynamicParticle.getPosition().get(0).getBounds();

			//add weights of new neuron
			int addPosition = inputLayerSize * (hiddenLayerSize-1);
			addPosition += (hiddenLayerSize-2)*(hiddenLayerSize-1)/2;
			for (int i = 0; i < inputLayerSize + hiddenLayerSize-1; ++i) {
				curDynamicParticle.getPosition().insert(addPosition, Real.valueOf(Double.NaN, bounds));
				curDynamicParticle.getBestPosition().insert(addPosition, Real.valueOf(Double.NaN, bounds));
				curDynamicParticle.getVelocity().insert(addPosition, Real.valueOf(Double.NaN, bounds));
			}

			//add weights between new neuron and output layer
			int startAddPosition = inputLayerSize * hiddenLayerSize;
			startAddPosition += (hiddenLayerSize-1)*hiddenLayerSize/2;
			for (int curOutput = 0; curOutput < outputLayerSize; ++curOutput) {
				addPosition = startAddPosition + (curOutput+1)*(inputLayerSize + hiddenLayerSize) -1;
				
				curDynamicParticle.getPosition().insert(addPosition, Real.valueOf(Double.NaN, bounds));
				curDynamicParticle.getBestPosition().insert(addPosition, Real.valueOf(Double.NaN, bounds));
				curDynamicParticle.getVelocity().insert(addPosition, Real.valueOf(Double.NaN, bounds));
			}
		}
    }
}
