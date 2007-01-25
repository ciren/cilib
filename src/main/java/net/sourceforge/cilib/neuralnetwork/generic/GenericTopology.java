/*
 * GenericTopology.java
 * 
 * Created on Jan 16, 2005
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
package net.sourceforge.cilib.neuralnetwork.generic;

import java.util.ArrayList;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronPipeline;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.GenericTopologyVisitor;

/**
 * @author stefanv
 *
 * This class serves as a generic topology class capable of representing almost any topology.  It is basically a 
 * multidimensional collection of NeuronConfig objects.  NeuronConfig objects are used to configure NeuronPipeline
 * objects (Flyweight design pattern).  A Typical NN would have about 3 or so NeuronPipelines i.e. Sigmoid, linear 
 * and Bias unit.
 * 
 * GenericTopologyVisitor objects are used to access NeuronConfig objects in a "left-to-right, top-to-bottom" 
 * fashion.  The Observer Pattern in also supported - clients just need to register.  This class forms part of the
 * Generic Framework and may be extended if needed.  It should work "as-is" in most cases.  There is also a 
 * getLayerIterator() method to manually traverse layers (NeuronConfig objects).
 * 
 */
public class GenericTopology implements NeuralNetworkTopology {
	
	ArrayList<NeuronPipeline> neuronPipePool = null;
	
	ArrayList<ArrayList<NeuronConfig>> layerList = null;
	
	ArrayList<Observer> observers = null;
	
	
	
	
	public GenericTopology(){
		//default contructor needed to construct TopologyDecorators
	}
	
		
	public GenericTopology(GenericTopologyBuilder tb){
		layerList = tb.createLayerList();
		neuronPipePool = tb.createNeuronPipePool();
		observers = new ArrayList<Observer>();
				
		System.out.println("\nPool size: " + neuronPipePool.size());
		
	}
	
			
	public ArrayList evaluate(NNPattern p){
		
		//int currentLayer = 0;
		
		ArrayList<Object> output = new ArrayList<Object>();
		
		for (int layer = 0; layer < layerList.size(); layer++){
			
			ArrayList<NeuronConfig> neuronList = layerList.get(layer);
		
			for (int i = 0; i < neuronList.size(); i++){
				
				NeuronConfig neuron = neuronList.get(i);
				
				Object result = (neuronPipePool.get(neuron.getNeuronPipelineIndex()) )
								.computeOutput(neuron, p);
				
				//Finalise the compute procedure
				neuron.setTminus1Output(neuron.getCurrentOutput());
				
				neuron.setCurrentOutput(result);
				
				if (neuron.isOutputNeuron())
					output.add(result);
			//	System.out.print("Neuron " + i + " in layer " + layer);
			//	System.out.println("\tResult in gentop.evaluate: " + ((Double)result).doubleValue());
				
			}//end for i
		
		}
			
		return output;
		
	}
		
	
	public ArrayList<Weight> getWeights(){
		//TODO: do this implementation as soon as the topology is stable via visitor
		return null;
	}	
	
	/**
	 * @return Returns the neuronPipePool.
	 */
	public ArrayList<NeuronPipeline> getNeuronPipePool() {
		return neuronPipePool;
	}
	/**
	 * @param neuronPipePool The neuronPipePool to set.
	 */
	public void setNeuronPipePool(ArrayList<NeuronPipeline> neuronPipePool) {
		this.neuronPipePool = neuronPipePool;
	}
	
	public void setWeights(ArrayList weights) throws TopologyException{
		//TODO: do this implementation as soon as the topology is stable via visitor
	}
	
	
	public int getNrLayers(){
		return layerList.size();
	}
	
	
	public StandardLayerIterator getLayerIterator(int layer){
		return new StandardLayerIterator(this.layerList.get(layer));		
	}
	
	public ArrayList<NeuronConfig> getLayer(int index){
		return layerList.get(index);
	}
	
	public void acceptVisitor(GenericTopologyVisitor v){
		
		for (int layer = 0; layer < layerList.size(); layer++){
			
			ArrayList<NeuronConfig> tmp = layerList.get(layer);
			for (int neuron = 0; neuron < tmp.size(); neuron++){
				v.visitNeuronConfig(tmp.get(neuron));
			}
		}
	}
	
	
	public void addObserver(Observer v){
		
		if (!observers.contains(v)){
			observers.add(v);
		}
		
	}
	
	
	public void removeObserver(Observer v){
		
		observers.remove(v);
	}
	
	
	public void notifyObservers(){
		for (int i = 0; i < observers.size(); i++){
			observers.get(i).validate();
		}
	}
	
}

