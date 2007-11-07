/*
 * Created on 2005/01/16
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.topologybuilders.GenericTopologyBuilder;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.GenericTopologyVisitor;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.RandomWeightInitialiser;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.SpecificWeightInitialiser;
import net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors.WeightExtractingVisitor;
import net.sourceforge.cilib.type.types.container.Vector;

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
public abstract class GenericTopology implements NeuralNetworkTopology, Initializable {
	
	ArrayList<ArrayList<NeuronConfig>> layerList = null;
	ArrayList<Observer> observers = null;
	
	private GenericTopologyBuilder topologyBuilder = null;
	private GenericTopologyVisitor weightInitialiser = null;
	
	
	public GenericTopology(){
		this.weightInitialiser = new RandomWeightInitialiser();
	}
	
		
	public void initialize(){
		
		if (this.topologyBuilder == null)
			throw new IllegalArgumentException("Required object was null during initialization");
						
		this.topologyBuilder.initialize();
		
		layerList = this.topologyBuilder.createLayerList();
		observers = new ArrayList<Observer>();
		
		this.acceptVisitor(this.weightInitialiser);
				
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
	
			
	public abstract Vector evaluate(NNPattern p);
		
	
	public ArrayList<NeuronConfig> getLayer(int index){
		return layerList.get(index);
	}
	
	
	public StandardLayerIterator getLayerIterator(int layer){
		return new StandardLayerIterator(this.layerList.get(layer));		
	}
		
	public int getNrLayers(){
		return layerList.size();
	}
		
	
	public Vector getWeights(){
		WeightExtractingVisitor w = new WeightExtractingVisitor();
		this.acceptVisitor(w);
		return w.getWeights();
	}	
	
	
	public void notifyObservers(){
		for (int i = 0; i < observers.size(); i++){
			observers.get(i).validate();
		}
	}
	
	
	public void removeObserver(Observer v){
		
		observers.remove(v);
	}
	
	
		
	public void setWeights(Vector weights){
								
		//set the weights array (NOT pattern input weights, only network weights
		SpecificWeightInitialiser v = new SpecificWeightInitialiser();
		v.setWeights(weights);
		this.acceptVisitor(v);
		
		if (!v.isEmpty()){
			throw new IllegalStateException("Weight vector size not compatible with topology - too few/many weights");
		}
	}


	public GenericTopologyBuilder getTopologyBuilder() {
		return topologyBuilder;
	}


	public void setTopologyBuilder(GenericTopologyBuilder topologyBuilder) {
		this.topologyBuilder = topologyBuilder;
	}
	
	public void printRepresentation(){
		
		for (int layer = 0; layer < layerList.size(); layer++){
			System.out.println("=== Layer " + layer + " ===");
			
			ArrayList<NeuronConfig> neuronList = layerList.get(layer);
			for (int i = 0; i < neuronList.size(); i++){
				System.out.println("Neuron " + i + ":");
				System.out.print("input Weights = " );
				if (neuronList.get(i).getInputWeights() != null){
					for (int w = 0; w < neuronList.get(i).getInputWeights().length; w++)
					System.out.print(neuronList.get(i).getInputWeights()[w].getWeightValue() + ", ");
				} else System.out.println(" NO INPUTS");
				System.out.println();
			}
		}
		System.out.println(" === end ===\n\n");
	}


	public void setWeightInitialiser(GenericTopologyVisitor weightInitialiser) {
		this.weightInitialiser = weightInitialiser;
	}


	
}

