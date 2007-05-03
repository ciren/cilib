/*
 * Created on 2005/01/21
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.topologybuilders;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 * This class froms the base for building any NN topology.  It provides a few basic tools such as contructing a 
 * layered topology, either connected or not connected.  This provides a base for createLayerList(). createLayerList()  
 * and createNeuronPipePool() are implemented in sub classes and together are responsible for building a valid 
 * topology (Builder Design Pattern).
 * 
 * 
 */
public abstract class GenericTopologyBuilder implements Initializable {
	
	public abstract ArrayList<ArrayList<NeuronConfig>> createLayerList();
	
	/*
	protected ArrayList<ArrayList<NeuronConfig>> constructConnectedLayeredTopology(int[] layerSizes, Weight baseW){
		
		ArrayList<ArrayList<NeuronConfig>> network = new ArrayList<ArrayList<NeuronConfig>>();
		
		ArrayList<NeuronConfig> tmp = new ArrayList<NeuronConfig>();
		
		//construct layer 0 as a base case
		for (int n = 0; n < layerSizes[0]; n++){
			NeuronConfig neuron = new NeuronConfig();
			tmp.add(neuron);
		}
		network.add(tmp);
		
		//construct more layers iteratively, and connect them to previous layer
		for (int layer = 1; layer < layerSizes.length; layer++){
			
			ArrayList<NeuronConfig> tmp2 = new ArrayList<NeuronConfig>();
			for (int n = 0; n < layerSizes[layer]; n++){
				NeuronConfig neuron = new NeuronConfig();
				
				Weight[] w = new Weight[network.get(layer-1).size()];
				for (int wi = 0; wi < w.length; wi++){
					w[wi] = baseW.clone();
				}
				neuron.setInputWeights(w);
				
				tmp2.add(neuron);
			}
			network.add(tmp2);
			
		}
		
		return network;
		
	}
	
	protected ArrayList<ArrayList<NeuronConfig>> constructLayeredTopology(int[] layerSizes){
		
		ArrayList<ArrayList<NeuronConfig>> network = new ArrayList<ArrayList<NeuronConfig>>();
		
		ArrayList<NeuronConfig> tmp = new ArrayList<NeuronConfig>();
		
		//construct layer 0 as a base case
		for (int n = 0; n < layerSizes[0]; n++){
			NeuronConfig neuron = new NeuronConfig();
			tmp.add(neuron);
		}
		network.add(tmp);
		
		//construct more layers recursively, no weights though...
		for (int layer = 1; layer < layerSizes.length; layer++){
			
			ArrayList<NeuronConfig> tmp2 = new ArrayList<NeuronConfig>();
			for (int n = 0; n < layerSizes[layer]; n++){
				NeuronConfig neuron = new NeuronConfig();
				tmp2.add(neuron);
			}
			network.add(tmp2);
		}
		return network;
	}
	
	*/
}
