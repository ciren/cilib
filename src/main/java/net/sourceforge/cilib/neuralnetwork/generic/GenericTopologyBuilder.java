/*
 * GenericTopologyBuilder.java
 * 
 * Created on Jan 21, 2005
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronPipeline;

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
public abstract class GenericTopologyBuilder {

	/**
	 * @return
	 */
	public abstract ArrayList<NeuronPipeline> createNeuronPipePool();

	
	/**
	 * @return
	 */
	public abstract ArrayList<ArrayList<NeuronConfig>> createLayerList();
	
	
	@SuppressWarnings("unchecked")
	protected ArrayList<ArrayList<NeuronConfig>> constructConnectedLayeredTopology(int[] layerSizes, Weight<Double> baseW){
		
		ArrayList<ArrayList<NeuronConfig>> network = new ArrayList<ArrayList<NeuronConfig>>();
		
		ArrayList<NeuronConfig> tmp = new ArrayList<NeuronConfig>();
		
		//construct layer 0 as a base case
		for (int n = 0; n < layerSizes[0]; n++){
			NeuronConfig neuron = new NeuronConfig();
			tmp.add(neuron);
		}
		network.add(tmp);
		
		//construct more layers recursively, and connect them to previous layer
		for (int layer = 1; layer < layerSizes.length; layer++){
			
			ArrayList<NeuronConfig> tmp2 = new ArrayList<NeuronConfig>();
			for (int n = 0; n < layerSizes[layer]; n++){
				NeuronConfig neuron = new NeuronConfig();
				
				Weight<Double>[] w = (Weight<Double>[]) Array.newInstance(Weight.class, network.get(layer-1).size());
				for (int wi = 0; wi < w.length;wi++){
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
	
	
}
