/*
 * FFNNgenericTopologyBuilder.java
 * 
 * Created on May 15, 2004
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
package net.sourceforge.cilib.neuralnetwork.testarea;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.GenericTopologyBuilder;
import net.sourceforge.cilib.neuralnetwork.generic.LayerIterator;
import net.sourceforge.cilib.neuralnetwork.generic.StandardLayerIterator;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.BiasNeuronPipeline;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.DotProductPipeline;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.LinearOutputFunction;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronPipeline;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FFNNgenericTopologyBuilder extends GenericTopologyBuilder {
	
	int[] layerSizes;
	
	Weight<Double> baseWeigt = null;
	

	/**
	 * @param layerSizes
	 * @param baseWeigt
	 */
	public FFNNgenericTopologyBuilder(int[] layerSizes, Weight<Double> baseWeigt) {
		super();
		if (layerSizes.length < 3){
			System.out.println("FFNNgenericTopologyBuilder: layers fewer than 3 not accepted");
			System.exit(1);
		}
		this.layerSizes = layerSizes;
		this.baseWeigt = baseWeigt;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.GenericTopologyBuilder#createNeuronPipePool()
	 */
	public ArrayList<NeuronPipeline> createNeuronPipePool() {
		
		ArrayList<NeuronPipeline> tmp = new ArrayList<NeuronPipeline>();
		
		tmp.add(new DotProductPipeline(new LinearOutputFunction())); //used by pattern input neuron
		tmp.add(new BiasNeuronPipeline());		
		tmp.add(new DotProductPipeline(new SigmoidOutputFunction()));
		
		
		return tmp;
		
	}
	

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.GenericTopologyBuilder#createLayerList()
	 */
	public ArrayList<ArrayList<NeuronConfig>> createLayerList() {
		
		//contruct a connected network with correct amount of layers and neurons.  Rest of network variables are null
		ArrayList<ArrayList<NeuronConfig>> network = this.constructConnectedLayeredTopology(layerSizes, baseWeigt);
		
		LayerIterator layerIter = new StandardLayerIterator(network.get(0));
		
		//configure input layer.
		for (NeuronConfig Li = layerIter.value(); layerIter.hasMore(); layerIter.nextNeuron()){
			
			Li = layerIter.value();
			
			if (layerIter.currentPosition() == layerIter.getNrNeurons() - 1){
				Li.setCurrentOutput(new Double(-1));
				Li.setTminus1Output(new Double(-1));
				Li.setInputWeights(null);
				
				//Always 1 as this is a bias unit.
				Li.setNeuronPipelineIndex(1);
				
			}
			else {
								
				boolean[] tsA = new boolean[1];
				tsA[0] = false;
				Li.setTimeStepMap(tsA);
				
				Li.setPatternWeight(new Weight<Double>(new Double(1), new Double(0)));
				
				Li.setPatternInputPos(layerIter.currentPosition());
				
				Li.setCurrentOutput(new Double(0));
				Li.setTminus1Output(new Double(0));
				
				//Always 0 as this is linear input from pattern
				Li.setNeuronPipelineIndex(0);
				Li.setOutputNeuron(false);
			}
			
		}
		//----------------------------------------------------------------------------------------------------
				
				
		//Iterate over each remaining layer, except output layer.
		for (int layer = 1; layer < layerSizes.length - 1; layer++){
			
			layerIter = new StandardLayerIterator(network.get(layer));
			
			//configure input layer.
			for (NeuronConfig Li = layerIter.value(); layerIter.hasMore(); layerIter.nextNeuron()){
				
				Li = layerIter.value();
				
				if (layerIter.currentPosition() == layerIter.getNrNeurons() - 1){
					Li.setCurrentOutput(new Double(-1));
					Li.setTminus1Output(new Double(-1));
					Li.setInputWeights(null);
					
					//Always 1 as this is a bias unit.
					Li.setNeuronPipelineIndex(1);
				}
				else {
					//set input neurons
					NeuronConfig[] inputs = new NeuronConfig[network.get(layer - 1).size()];
					for (int inp = 0; inp < inputs.length; inp++){
						inputs[inp] = network.get(layer - 1).get(inp);
					}
					Li.setInput(inputs);
					
					//set time step values to false
					boolean[] tsH = new boolean[network.get(layer - 1).size()];
					for (int inp = 0; inp < inputs.length; inp++){
						tsH[inp] = false;
					}
					Li.setTimeStepMap(tsH);
					
					Li.setCurrentOutput(new Double(0));
					Li.setTminus1Output(new Double(0));
					
					//2 for the moment, as this can be configured dotproduct.  Math needs to be checked 
					//anyway if multiple layers of SU and PU units are needed...  Multiple layers of SU
					//are supported.
					Li.setNeuronPipelineIndex(2);
					Li.setOutputNeuron(false);
					
				}//end else
			}//end for a layer
			
			
		} //end for all layers
		
		
		//-------------------------------------------------------------------------------------
		
		//Output layer
		layerIter = new StandardLayerIterator(network.get(network.size() - 1));
		
		
		for (NeuronConfig Li = layerIter.value(); layerIter.hasMore(); layerIter.nextNeuron()){
			
			Li = layerIter.value();
			
			//set input neurons
			NeuronConfig[] inputs = new NeuronConfig[network.get(network.size() - 2).size()];
			for (int inp = 0; inp < inputs.length; inp++){
				inputs[inp] = network.get(network.size() - 2).get(inp);
			}
			Li.setInput(inputs);
			
			//set time step values to false
			boolean[] tsH = new boolean[network.get(network.size() - 2).size()];
			for (int inp = 0; inp < inputs.length; inp++){
				tsH[inp] = false;
			}
			Li.setTimeStepMap(tsH);
			
			Li.setCurrentOutput(new Double(0));
			Li.setTminus1Output(new Double(0));
			
			//2 for the moment, as this can be configured dotproduct.  Math needs to be checked 
			//anyway if multiple layers of SU and PU units are needed...  Multiple layers of SU
			//are supported.
			Li.setNeuronPipelineIndex(2);
			Li.setOutputNeuron(true);
			
			
		}//end for a layer
		
		return network;
	} //end method

}
