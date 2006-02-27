/*
 * NeuronConfig.java
 * 
 * Created on Mar 21, 2005
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;

/**
 * @author stefanv
 *
 * This class forms the base for any Neuron in a NN topology.  It forms the extrinsic state to a NeuronPipeline
 * object.  It also keeps track of the Neurons current and previous output and so doing supports recurrent 
 * architectures.
 * 
 * It forms part of the Generic Framework and should mostly be used "as-is".  In specific cases it can be extended, but an extension may not work 
 * for all cases...
 * 
 */
public class NeuronConfig {
	
	//Output array: current output
	//   			timestep t-1 output
	Object currentOutput = null;
	
	//Inputs from other neurons in the net
	NeuronConfig[] input = null;
	
	//the corresponding weights for input
	Weight<Double>[] inputWeights = null;
	
//	//Layer of neuron, negative = uninitialised
//	int layer = -999;
	
	//Index to Neuron pipeline of neuron in GenericTopology
	int neuronPipelineIndex = -999;
	
	//indice of input from NNPattern - negative = not used
	int patternInputPos = -999;
	
	//The weight used by the pattern input if applicable
	Weight<Double> patternWeight = null;
	
	//Indicates whether to use input[i]'s current output (0) or timestep t-1 output (1).
	//This is used mostly in Recurrent architectures with self connections
	boolean[] timeStepMap = null;
	
	Object Tminus1Output = null;
	
	//true if the neuron is an output of the network
	//used to indicate which neurons in which layers are also output neurons.  This is
	//because in some topologies like Hopfield nets, neurons are input, output and hidden 
	//neurons simultaneously
	boolean isOutputNeuron = false;
	
	
	public NeuronConfig() {
		super();
		//default values - Topology builder needs to explicitely change this value for each neuron
		this.neuronPipelineIndex = -999;
		this.input = null;
		this.inputWeights = null;
		this.timeStepMap = null;
		this.patternInputPos = -999;
		this.patternWeight = null;
		
		
		currentOutput = null;
		Tminus1Output = null;
	}
	
	
	/**
	 * @param neuronPipelineIndex
	 */
	public NeuronConfig(int pipeIndex, Object initValC, Object initValT) {
		//used mostly for neuron types with a fixed output, and no fan-in such as bias units, etc.
		this.neuronPipelineIndex = pipeIndex;
		this.input = null;
		this.inputWeights = null;
		this.timeStepMap = null;
		this.patternInputPos = -999;
		this.patternWeight = null;
		
		currentOutput = initValC;
		Tminus1Output = initValT;
	}
		/**
	 * @param input
	 * @param inputWeights
	 * @param timeStepMap
	 * @param patternInput
	 * @param patternWeight
	 * @param neuronPipelineIndex
	 * @param layer
	 */
	public NeuronConfig(NeuronConfig[] input, 
			            Weight<Double>[] inputWeights,
			            boolean[] timeStepMap, 
			            int patternInput, 
			            Weight<Double> patternWeight,
			            Object initialOutput,
			            int pipeIndex) {
		
		//Full blown constructor, used to fully initialise any neuron 
		
		this.input = input;
		this.inputWeights = inputWeights;
		this.timeStepMap = timeStepMap;
		this.patternInputPos = patternInput;
		this.patternWeight = patternWeight;
		//this.layer = layer;
		this.neuronPipelineIndex = pipeIndex;
		currentOutput = initialOutput;
		Tminus1Output = initialOutput;
		
		System.out.println("\nNeuron Config: -------------------------");
		if (input != null) System.out.println("input size: " + input.length);
		if (inputWeights != null) System.out.println("input weight size: " + inputWeights.length);
		System.out.println("timestepmap size: " + timeStepMap.length);
		System.out.println("Pattern pos: " + patternInputPos);
		if (patternWeight != null)System.out.println("pattern weight: " + patternWeight.toString());
		//System.out.println("layer: " + layer);
		System.out.println("Current output: " + currentOutput);
	}
	
	
	
	/**
	 * @return Returns the currentOutput.
	 */
	public Object getCurrentOutput() {
		return currentOutput;
	}
	/**
	 * @return Returns the input.
	 */
	public NeuronConfig[] getInput() {
		return input;
	}
	/**
	 * @return Returns the inputWeights.
	 */
	public Weight<Double>[] getInputWeights() {
		return inputWeights;
	}
				
	/**
	 * @return Returns the neuronPipelineIndex.
	 */
	public int getNeuronPipelineIndex() {
		return neuronPipelineIndex;
	}
	
	/**
	 * @return Returns the patternInput.
	 */
	public int getPatternInputPos() {
		return patternInputPos;
	}
	/**
	 * @return Returns the patternWeight.
	 */
	public Weight<Double> getPatternWeight() {
		return patternWeight;
	}
	/**
	 * @return Returns the timeStepMap.
	 */
	public boolean[] getTimeStepMap() {
		return timeStepMap;
	}
	/**
	 * @return Returns the tminus1Output.
	 */
	public Object getTminus1Output() {
		return Tminus1Output;
	}
	/**
	 * @param currentOutput The currentOutput to set.
	 */
	public void setCurrentOutput(Object currentOutput) {
		this.currentOutput = currentOutput;
	}
	/**
	 * @param input The input to set.
	 */
	public void setInput(NeuronConfig[] input) {
		this.input = input;
	}
	/**
	 * @param inputWeights The inputWeights to set.
	 */
	public void setInputWeights(Weight<Double>[] inputWeights) {
		this.inputWeights = inputWeights;
	}
	
	/**
	 * @param neuronPipelineIndex The neuronPipelineIndex to set.
	 */
	public void setNeuronPipelineIndex(int neuronPipelineIndex) {
		this.neuronPipelineIndex = neuronPipelineIndex;
	}
	/**
	 * @param patternInput The patternInput to set.
	 */
	public void setPatternInputPos(int patternInput) {
		this.patternInputPos = patternInput;
	}
	/**
	 * @param patternWeight The patternWeight to set.
	 */
	public void setPatternWeight(Weight<Double> patternWeight) {
		this.patternWeight = patternWeight;
	}
	/**
	 * @param timeStepMap The timeStepMap to set.
	 */
	public void setTimeStepMap(boolean[] timeStepMap) {
		this.timeStepMap = timeStepMap;
	}
	/**
	 * @param tminus1Output The tminus1Output to set.
	 */
	public void setTminus1Output(Object tminus1Output) {
		Tminus1Output = tminus1Output;
	}
	
	
	/**
	 * @return Returns the isOutputNeuron.
	 */
	public boolean isOutputNeuron() {
		return isOutputNeuron;
	}
	/**
	 * @param isOutputNeuron The isOutputNeuron to set.
	 */
	public void setOutputNeuron(boolean isOutputNeuron) {
		this.isOutputNeuron = isOutputNeuron;
	}
}
