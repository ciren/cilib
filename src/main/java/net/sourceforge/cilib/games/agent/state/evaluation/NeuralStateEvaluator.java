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
package net.sourceforge.cilib.games.agent.state.evaluation;


import java.util.List;

import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.agent.neural.NeuralOutputInterpretationStrategy;
import net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
/**
 * @author leo
 * This is a {@linkplain StateEvaluator} that uses a Neural Network to evaluate a given game state. This class inherets from the NeuralAgent class
 * becuase it's functionality is very similar.
 */
public class NeuralStateEvaluator extends NeuralAgent implements StateEvaluator {
    /**
     *
     */
    private static final long serialVersionUID = 7781748822862754078L;

    /**
     *
     */
    public NeuralStateEvaluator() {
    }

    /**
     * Copy Constructor
     * @param other
     */
    public NeuralStateEvaluator(NeuralStateEvaluator other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    public double evaluateState(Game<GameState> state, int decisionPlayerID) {
        Vector input = stateInputStrategy.getNeuralInputArray(this, state);
        StandardPattern pattern = new StandardPattern(input, input);
        //get the output vector
        Vector NNOutput = neuralNetwork.evaluatePattern(pattern);//perform NN iteration, get output
        return  NNOutput.get(0).getReal();
    }

    /**
     * {@inheritDoc}
     */
    public DomainRegistry getEvaluatorDomain() {
        return this.getAgentDomain();
    }

    /**
     * {@inheritDoc}
     */
    public void initializeEvaluator(Type evaluatorData) {
        this.initializeAgent(evaluatorData);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public NeuralStateEvaluator getClone() {
        return new NeuralStateEvaluator(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStateInputStrategy(NeuralStateInputStrategy inputStrategy){
        this.stateInputStrategy = inputStrategy;
        if(inputStrategy != null)
            initializeNeuralNetwork();
    }

    /**
     * The {@linkplain NeuralOutputInterpretationStrategy} should not be set since this class funtions only as a state evaluator. A single output
     * Neuron is used to rank the given state.
     */
    @Override
    public void setOutputInterpretationStrategy(NeuralOutputInterpretationStrategy outputStrategy){
        throw new RuntimeException("Unable to set output interpretation strategy for neural state evaluation agent. Leave as null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move(Game game) {
        throw new RuntimeException("This method should not be called.");
    }

    /**
     * {@inheritDoc}
     */
    private void initializeNeuralNetwork(){
        List<LayerConfiguration> layerConfigs = neuralNetwork.getArchitecture().getArchitectureBuilder().getLayerConfigurations();
        //set input layer, first layer
        layerConfigs.get(0).setSize(stateInputStrategy.amountInputs());
        //set output layer, last layer
        layerConfigs.get(layerConfigs.size() - 1).setSize(1); //there will only be 1 output neuron, the rank value of the state

        neuralNetwork.initialize();
    }
}
