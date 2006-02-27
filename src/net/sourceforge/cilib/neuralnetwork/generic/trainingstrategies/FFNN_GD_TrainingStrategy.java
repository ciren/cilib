/*
 * FFNN_GD_TrainingStrategy.java
 * 
 * Created on Feb 20, 2005
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
package net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.LayerIterator;
import net.sourceforge.cilib.neuralnetwork.generic.Observer;
import net.sourceforge.cilib.neuralnetwork.generic.TopologyException;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 * This is an Application in the Generic Framework - it does not form part of the framework.  It implements multi layer Gradient Decent (GD) training
 * for Feedforward NN (FFNN).  To use the class, call the invokeTrainer() method with the last presented
 * pattern as an argument.
 */
public class FFNN_GD_TrainingStrategy implements TrainingStrategy, Observer {
	
	GenericTopology topology = null;
	ErrorSignal delta = null;
	Double learningRate = null;
	Double momentum = null;
		
	/**
	 * @param topology
	 */
	public FFNN_GD_TrainingStrategy(GenericTopology topology_, ErrorSignal delta_, double lr, double momentum_) {
		this.topology = topology_;
		this.delta = delta_;
		this.learningRate = new Double(lr);
		this.momentum = new Double(momentum_); 
	}
	
	
	public void updateFanInWeights(NeuronConfig n, Object delta_) throws TopologyException{
		//Wi(t) = Wi(t) + learnRate * delta * input_i    +    momentum * change_Wi(t-1) 
		
		//for the case where neuron has neuron-to-neuron inputs
		Weight<Double>[] w = n.getInputWeights();
		Double temp = -1 * learningRate * (Double)delta_;
		
		for (int i = 0; i < w.length; i++){
			
			Double change = temp * (Double)n.getInput()[i].getCurrentOutput();  
			
			w[i].setWeightValue((Double)w[i].getWeightValue() + 
								change +
								(this.momentum * (Double)w[i].getPreviousChange()));
	//		System.out.println("Weight after change = " + (Double)w[i].getWeightValue() + change);
			
			//System.out.println("change only = " + change);
			
			w[i].setPreviousChange(change);
		}
		
		//for the case where the neuron has pattern-to-neuron inputs
		//should never happen as these lie in layer 0, which is never evaluated
		if (n.getPatternWeight() != null){
			throw new TopologyException("Topology is not compatible with Gradient Decent - " +
										"pattern input neuron has other inputs - thus recurrent");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.TrainingStrategy#invokeTrainer(java.lang.Object)
	 */
	public void invokeTrainer(Object args) {
		
		//System.out.println("In InvokeTrainer method of GD...");
		
		NNPattern p = (NNPattern)args;
		ArrayList<NeuronConfig> updateNeuronList = new ArrayList<NeuronConfig>();
		ArrayList<Object> deltaUpdateList = new ArrayList<Object>();
				
		//iterate over output layer
		LayerIterator outputIter = topology.getLayerIterator(topology.getNrLayers() - 1);
		ArrayList<Object> prevDeltaList = new ArrayList<Object>(outputIter.getNrNeurons());
		LayerIterator prevIter = null;
		
		for (NeuronConfig Oi = outputIter.value(); outputIter.hasMore(); outputIter.nextNeuron()){
			Oi = outputIter.value();
		
			//for each output, find deltaOi definition using 
			//error, output and activation derivative
			Object tmpO = delta.computeBaseDelta(p.getTarget().get(outputIter.currentPosition()), 
											  	 Oi.getCurrentOutput(), 
											  	 topology.getNeuronPipePool().get(Oi.getNeuronPipelineIndex())
											  	 .computeOutputFunctionDerivativeUsingLastOutput(Oi.getCurrentOutput()) );
		
			prevDeltaList.add(tmpO);
			updateNeuronList.add(Oi);
			deltaUpdateList.add(tmpO);
		} //end for Oi
		
		prevIter = outputIter;
		//-----------------------------------------------------------------------------
		
		for (int layer = topology.getNrLayers() - 2; layer > 0; layer--){
		//Iterate backwards over all remaining layers L = #nrlayers-1 ... 1.  layer 0 not done as input layer
		
			LayerIterator layerIter = topology.getLayerIterator(layer);
			ArrayList<Object> DeltaList = new ArrayList<Object>(layerIter.getNrNeurons());
			
		//	System.out.println("neuron in layer: " + layer + " = " + layerIter.getNrNeurons());
		//	for (NeuronConfig Li = layerIter.value(); layerIter.hasMore(); layerIter.nextNeuron()){
		//		System.out.println("neuron "+ layerIter.currentPosition()+" output is = " + layerIter.value().getCurrentOutput()); 
		//		System.out.println("neuron 2 output is = " + layerIter.value().getCurrentOutput()); layerIter.nextNeuron();
		//		System.out.println("neuron 3 output is = " + layerIter.value().getCurrentOutput());
		//	}
			//iterate over all neurons in layer L			
			for (NeuronConfig Li = layerIter.value(); layerIter.hasMore(); layerIter.nextNeuron()){
				
				Li = layerIter.value();
				
				//Compute deltaLi for neuron i in layer L, taking recursive 
				//definition into account.
				//Only applicable to neurons that backprop (i.e. not Bias unit).
				//Thus if Li has fanin weights array with length > 1 then perform backprop
				
				if (Li.getInputWeights() != null){
								
					//Weights out of current layer neuron to layer + 1 neurons
					ArrayList<Weight> w = new ArrayList<Weight>();
					prevIter.reset();
					//System.out.println("Prev Iter size = " + prevIter.getNrNeurons());
					//System.out.println("Weight from out1 to unit = " + prevIter.value().getInputWeights()[layerIter.currentPosition()].getWeightValue());
					
					for (NeuronConfig Wi = prevIter.value(); prevIter.hasMore(); prevIter.nextNeuron()){
						w.add(Wi.getInputWeights()[layerIter.currentPosition()]);
						//TODO: Fix this up - using static indexing, make dynamic looking for Li in Wi inputlist
						//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5
					}
					
					Object tmpL = delta.computeRecursiveDelta(
							topology.getNeuronPipePool().get(Li.getNeuronPipelineIndex())
							.computeOutputFunctionDerivativeUsingLastOutput(Li.getCurrentOutput()),
							prevDeltaList,
							w,
							Li.getCurrentOutput());
					
					DeltaList.add(tmpL);
					
					
					updateNeuronList.add(Li);
					deltaUpdateList.add(tmpL);
					
				} //ebd if
				
				
			} //end for Li	
			
			prevIter = layerIter;
		
		} //end for layer	
		
		//update weights for each output neuron using deltaOi
		for (int count = 0; count < updateNeuronList.size(); count++){
			try {
				this.updateFanInWeights(updateNeuronList.get(count), deltaUpdateList.get(count));
			} catch (TopologyException e) {
				System.out.println("Error in GD trainer invokdeTrainer method, output layer: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void validate(){
		//Follows the Observer design pattern.  Any change in GenericTopology should call notify, which
		//calls all Observers' validate() method, including this class's.
		//This method should perform a full check on the topology to see if it is still compatible with
		//this trainer.  If not an exception should be thrown.
		
		
		
		//check layering right
		
		//check no pattern inputs anywere else than in layer 0
	}
	

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.TrainingStrategy#preEpochActions(java.lang.Object)
	 */
	public void preEpochActions(Object args) {
	
		
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.TrainingStrategy#postEpochActions(java.lang.Object)
	 */
	public void postEpochActions(Object args) {
		
		
	}

}
