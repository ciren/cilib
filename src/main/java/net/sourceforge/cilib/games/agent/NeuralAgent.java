/*
 * Copyright (C) 2003 - 2008
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
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.FFNNgenericTopologyBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
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
	NeuralStateInputStrategy inputStrategy;
	//this determines how the output of the Neural Network alters the game state
	NeuralOutputInterpretationStrategy outputStrategy;
	int amHiddenNodes;
	//FFNNTopology nnet;
	GenericTopology neuralNetwork;
	/**
	 * @param playerNo
	 */
	public NeuralAgent() {
		super();
		// TODO Auto-generated constructor stub
		amHiddenNodes = 1;
	}

	/**
	 * @param other
	 */
	public NeuralAgent(NeuralAgent other) {
		super(other);
		inputStrategy = other.inputStrategy;
		outputStrategy = other.outputStrategy;
		amHiddenNodes = other.amHiddenNodes;
		neuralNetwork = other.neuralNetwork; //CLONE?
	}

	private void initializeNeuralNetwork(){
		((FFNNgenericTopologyBuilder)neuralNetwork.getTopologyBuilder()).addLayer(inputStrategy.amountInputs() + 1);
		((FFNNgenericTopologyBuilder)neuralNetwork.getTopologyBuilder()).addLayer(amHiddenNodes + 1);
		((FFNNgenericTopologyBuilder)neuralNetwork.getTopologyBuilder()).addLayer(outputStrategy.getAmOutputs());
		neuralNetwork.initialize();
	}

	public void setHiddenNodes(int amount){
		amHiddenNodes = amount;
		if(inputStrategy != null && outputStrategy != null)
			initializeNeuralNetwork();
	}

	public void setStateInputStrategy(NeuralStateInputStrategy inputStrategy){
		this.inputStrategy = inputStrategy;
		if(inputStrategy != null && outputStrategy != null)
			initializeNeuralNetwork();
	}

	public void setOutputInterpretationStrategy(NeuralOutputInterpretationStrategy outputStrategy){
		this.outputStrategy = outputStrategy;
		if(inputStrategy != null && outputStrategy != null)
			initializeNeuralNetwork();
	}

	public void setWeights(Vector weights){
		//set the NN weights to the specified weight vector
		neuralNetwork.setWeights(weights);
	}

	public double getScaledInput(double val, double min, double max){
		 //scale to active range of activation function. need to get this from neural network object
		double scaledMin = -1.0;
		return (((val - min) * (1.0 - scaledMin)) / (max - min)) + scaledMin;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.agent.Agent#getClone()
	 */
	@Override
	public Agent getClone() {
		// TODO Auto-generated method stub
		return new NeuralAgent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void move(Game game) {
		//set the input of the neural network to
		Vector input = inputStrategy.getNeuralInputArray(this, game);
		StandardPattern pattern = new StandardPattern(input, input);
		//get the output vector
		Vector NNOutput = neuralNetwork.evaluate(pattern);//perform NN iteration, get output
		outputStrategy.applyOutputToState(NNOutput, this, game);
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
		String representation = "R(-" + activationRange + ", " + activationRange + ")^" + neuralNetwork.getWeights().size(); //need to get min and max as well?!?
		agentDomain.setDomainString(representation);
		return agentDomain;
	}

	public void setNeuralNetworkTopology(GenericTopology topology){
		neuralNetwork = topology;
	}


}
