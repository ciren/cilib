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
package net.sourceforge.cilib.games.agent.state.evaluation;


import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.agent.neural.NeuralOutputInterpretationStrategy;
import net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.FFNNgenericTopologyBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

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
        TypeList NNOutput = neuralNetworkTopology.evaluate(pattern);//perform NN iteration, get output
        return ((Numeric) NNOutput.get(0)).getReal();
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
    public void setHiddenNodes(int count){
        hiddenNodesCount = count;
        if(stateInputStrategy != null)
            initializeNeuralNetwork();
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
        ((FFNNgenericTopologyBuilder)neuralNetworkTopology.getTopologyBuilder()).addLayer(stateInputStrategy.amountInputs() + 1);
        ((FFNNgenericTopologyBuilder)neuralNetworkTopology.getTopologyBuilder()).addLayer(hiddenNodesCount + 1);
        ((FFNNgenericTopologyBuilder)neuralNetworkTopology.getTopologyBuilder()).addLayer(1); //there will only be 1 output neuron, the rank value of the state
        neuralNetworkTopology.initialize();
    }
}
