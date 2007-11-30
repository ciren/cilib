/*
 * Created on 2005/05/15
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.topologybuilders;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.BiasNeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.DotProductNeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.LinearOutputFunction;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.SigmoidOutputFunction;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author stefanv
 *
 */
public class FFNNgenericTopologyBuilder extends GenericTopologyBuilder{
	
	int[] layerSizes;
	Weight prototypeWeight = null;
	
	
	public FFNNgenericTopologyBuilder() {
		super();
		this.prototypeWeight = null;
		this.layerSizes = null;
	}
	
	
	public void initialize(){
		
		if ((this.layerSizes == null) || (this.prototypeWeight == null)){
			throw new IllegalArgumentException("FFNNGenericTopologyBuilder: Required object was null during initialization");
		}
		
		//check all but last layer sizes - minimum is 2 as bias units are included.
		for (int i = 0; i < layerSizes.length - 1; i++){
			if (this.layerSizes[i] <= 1) {
				throw new IllegalArgumentException("FFNNGenericTopologyBuilder: illegal layer size. Layer " + i + " size = " + this.layerSizes[i] 
				                                                                                                                               + ".  Total layers = " + this.layerSizes.length + ".  Minimum layer size is 2 as bias units are included.");
			}
		}
	}
	
	public ArrayList<ArrayList<NeuronConfig>> createLayerList(){
		
		ArrayList<ArrayList<NeuronConfig>> network = new ArrayList<ArrayList<NeuronConfig>>();
		
		ArrayList<NeuronConfig> tmp = new ArrayList<NeuronConfig>();
		
		//construct layer 0 as a base case
		for (int n = 0; n < layerSizes[0] - 1; n++){
			DotProductNeuronConfig neuron = new DotProductNeuronConfig();
			neuron.setOutputFunction(new LinearOutputFunction());
			boolean[] tsA = new boolean[1];
			tsA[0] = false;
			neuron.setTimeStepMap(tsA);
			neuron.setPatternWeight(new Weight(new Real(1)));	
			neuron.setPatternInputPos(n);
			neuron.setCurrentOutput(new Real(0));
			neuron.setTminus1Output(new Real(0));
			neuron.setOutputNeuron(false);
			tmp.add(neuron);
		}
		//add bias neuron
		BiasNeuronConfig biasNeuron = new BiasNeuronConfig();
		biasNeuron.setCurrentOutput(new Real(-1));
		biasNeuron.setTminus1Output(new Real(-1));
		biasNeuron.setInputWeights(null);
		tmp.add(biasNeuron);
		
		network.add(tmp);
				
		//---------------------------------------------------------------------
		//construct more layers iteratively, and connect them to previous layer
		for (int layer = 1; layer < layerSizes.length; layer++){
			
			ArrayList<NeuronConfig> tmp2 = new ArrayList<NeuronConfig>();
			for (int n = 0; n < layerSizes[layer] - 1; n++){
				DotProductNeuronConfig neuron2 = new DotProductNeuronConfig();
				neuron2.setOutputFunction(new SigmoidOutputFunction());
				
				//set input neurons
				NeuronConfig[] inputs = new NeuronConfig[network.get(layer - 1).size()];
				for (int inp = 0; inp < inputs.length; inp++){
					inputs[inp] = network.get(layer - 1).get(inp);
				}
				neuron2.setInput(inputs);
				
				//set input weights
				Weight[] w = new Weight[network.get(layer-1).size()];
				for (int wi = 0; wi < w.length; wi++){
					w[wi] = prototypeWeight.getClone();
				}
				neuron2.setInputWeights(w);
				
				//set time step values to false
				boolean[] tsH = new boolean[network.get(layer - 1).size()];
				for (int inp = 0; inp < inputs.length; inp++){
					tsH[inp] = false;
				}
				neuron2.setTimeStepMap(tsH);
				neuron2.setCurrentOutput(new Real(0));
				neuron2.setTminus1Output(new Real(0));
				if (layer < (layerSizes.length -1)){
					neuron2.setOutputNeuron(false);
				} else neuron2.setOutputNeuron(true);
							
				tmp2.add(neuron2);
			}
			
			//add bias neuron, but only if not last layer, else normal neurons
			if (layer < (layerSizes.length -1)){
				BiasNeuronConfig biasNeuron2 = new BiasNeuronConfig();
				biasNeuron2.setCurrentOutput(new Real(-1));
				biasNeuron2.setTminus1Output(new Real(-1));
				biasNeuron2.setInputWeights(null);
				tmp2.add(biasNeuron2);
			}
			else {
				DotProductNeuronConfig neuron2 = new DotProductNeuronConfig();
				neuron2.setOutputFunction(new SigmoidOutputFunction());
				
				//set input neurons
				NeuronConfig[] inputs = new NeuronConfig[network.get(layer - 1).size()];
				for (int inp = 0; inp < inputs.length; inp++){
					inputs[inp] = network.get(layer - 1).get(inp);
				}
				neuron2.setInput(inputs);
				
				//set input weights
				Weight[] w = new Weight[network.get(layer-1).size()];
				for (int wi = 0; wi < w.length; wi++){
					w[wi] = prototypeWeight.getClone();
				}
				neuron2.setInputWeights(w);
				
				//set time step values to false
				boolean[] tsH = new boolean[network.get(layer - 1).size()];
				for (int inp = 0; inp < inputs.length; inp++){
					tsH[inp] = false;
				}
				neuron2.setTimeStepMap(tsH);
				neuron2.setCurrentOutput(new Real(0));
				neuron2.setTminus1Output(new Real(0));
				neuron2.setOutputNeuron(true);
							
				tmp2.add(neuron2);
			}
			network.add(tmp2);
			
		}
		
		return network;
		
	}
	
	
	public void setPrototypeWeight(Weight pw) {
		this.prototypeWeight = pw;
	}
	
	
	//Used to add the array of layer sizes in XML
	public void addLayer(int nrNeurons){
		
		if (nrNeurons <= 0){
			throw new IllegalArgumentException("FFNNgenericTopologyBuilder: Invalid nr of neurons in layer: " + nrNeurons);
		}
		
		if (this.layerSizes == null){
			this.layerSizes = new int[1];
			this.layerSizes[0] = nrNeurons;
		} 
		else {
			int[] tmp = new int[this.layerSizes.length + 1];
			
			for (int i=0; i < layerSizes.length; i++){
				tmp[i] = layerSizes[i];			
			}	
			tmp[layerSizes.length] = nrNeurons;
			this.layerSizes = tmp;
		}
	}
	
	public void setAddLayer(int nr){
		this.addLayer(nr);
	}
}
