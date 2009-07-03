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
package net.sourceforge.cilib.games.agent;


import net.sourceforge.cilib.games.agent.neural.NeuralOutputInterpretationStrategy;
import net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.FFNNgenericTopologyBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * @author leo
 * An agent that uses a Neural Network to make decisions
 */
public class NeuralAgent extends Agent {
    /**
     *
     */
    private static final long serialVersionUID = 5910765539852468020L;
    //this determines how the game state is given to the Neural Network as input
    protected NeuralStateInputStrategy stateInputStrategy;
    //this determines how the output of the Neural Network alters the game state
    protected NeuralOutputInterpretationStrategy outputInterpretationStrategy;
    //The amount of hidden nodes for the NN
    protected int hiddenNodesCount;
    //FFNNTopology nnet;
    protected GenericTopology neuralNetworkTopology;
    /**
     * @param playerNo
     */
    public NeuralAgent() {
        super();
        hiddenNodesCount = 1;
    }

    /**
     * @param other
     */
    public NeuralAgent(NeuralAgent other) {
        super(other);
        stateInputStrategy = other.stateInputStrategy;
        outputInterpretationStrategy = other.outputInterpretationStrategy;
        hiddenNodesCount = other.hiddenNodesCount;
        neuralNetworkTopology = other.neuralNetworkTopology; //CLONE?
    }

    private void initializeNeuralNetwork(){
        ((FFNNgenericTopologyBuilder)neuralNetworkTopology.getTopologyBuilder()).addLayer(stateInputStrategy.amountInputs() + 1);
        ((FFNNgenericTopologyBuilder)neuralNetworkTopology.getTopologyBuilder()).addLayer(hiddenNodesCount + 1);
        ((FFNNgenericTopologyBuilder)neuralNetworkTopology.getTopologyBuilder()).addLayer(outputInterpretationStrategy.getAmOutputs());
        neuralNetworkTopology.initialize();
    }

    public void setHiddenNodes(int count){
        hiddenNodesCount = count;
        if(stateInputStrategy != null && outputInterpretationStrategy != null)
            initializeNeuralNetwork();
    }

    public void setStateInputStrategy(NeuralStateInputStrategy stateInputStrategy){
        this.stateInputStrategy = stateInputStrategy;
        if(stateInputStrategy != null && outputInterpretationStrategy != null)
            initializeNeuralNetwork();
    }

    public void setOutputInterpretationStrategy(NeuralOutputInterpretationStrategy outputInterpretationStrategy){
        this.outputInterpretationStrategy = outputInterpretationStrategy;
        if(stateInputStrategy != null && outputInterpretationStrategy != null)
            initializeNeuralNetwork();
    }

    public void setWeights(Vector weights){
        //set the NN weights to the specified weight vector
        neuralNetworkTopology.setWeights(weights);
    }
    /**
     * Scale the input value from the specified range to the active range of the input nodes for the NN
     * @param val the value to scale
     * @param min the lower bound
     * @param max the upper bound
     * @return
     */
    public double getScaledInput(double val, double min, double max){
         //scale to active range of activation function. need to get this from neural network object
        double scaledMax = neuralNetworkTopology.getTopologyBuilder().getActivationFunction().getUpperActiveRange();
        double scaledMin = neuralNetworkTopology.getTopologyBuilder().getActivationFunction().getLowerActiveRange();
        return (((val - min) * (scaledMax - scaledMin)) / (max - min)) + scaledMin;
        //return val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Agent getClone() {
        return new NeuralAgent(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move(Game<GameState> game) {
        //set the input of the neural network to
        Vector input = stateInputStrategy.getNeuralInputArray(this, game);
        StandardPattern pattern = new StandardPattern(input, input);
        //get the output vector
        TypeList NNOutput = neuralNetworkTopology.evaluate(pattern);//perform NN iteration, get output
        outputInterpretationStrategy.applyOutputToState(NNOutput, this, game);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeAgent(Type agentData) {
        if(!(agentData instanceof Vector))
            throw new RuntimeException("agentData is not a weight vector, unable to initialize Neural Agent");
        setWeights((Vector)agentData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainRegistry getAgentDomain() {
        DomainRegistry agentDomain = new StringBasedDomainRegistry();
        double activationRange = 1.0;//1 / Math.sqrt(inputStrategy.amountInputs());
        String representation = "R(-" + activationRange + ", " + activationRange + ")^" + neuralNetworkTopology.getWeights().size(); //need to get min and max as well?!?
        agentDomain.setDomainString(representation);
        return agentDomain;
    }

    public void setNeuralNetworkTopology(GenericTopology topology){
        neuralNetworkTopology = topology;
    }

    public int getHiddenNodesCount() {
        return hiddenNodesCount;
    }

    public NeuralOutputInterpretationStrategy getOutputInterpretationStrategy() {
        return outputInterpretationStrategy;
    }

    public NeuralStateInputStrategy getStateInputStrategy() {
        return stateInputStrategy;
    }

    public GenericTopology getNeuralNetworkTopology() {
        return neuralNetworkTopology;
    }


}
