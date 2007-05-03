/*
 * Created on 2005/04/06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 * A Standard iterator for traversing a layer of NeuronConfoig objects in order.
 */
public class StandardLayerIterator implements LayerIterator {

	
	int currentNeuron = 0;
	ArrayList<NeuronConfig> layer = null;
	
	
	
	public StandardLayerIterator(ArrayList<NeuronConfig> layer_) {
		layer = layer_;
		currentNeuron = 0;
	}
	
	
	public void nextNeuron() {
		currentNeuron++;
	}

	
	public NeuronConfig value() {
		return layer.get(currentNeuron);
	}

	
	
	public int getNrNeurons() {
		return layer.size();
	}
	
	public boolean hasMore(){
		return currentNeuron < layer.size();
	}


	
	public void reset() {
		currentNeuron = 0;
	}


	
	public int currentPosition() {
		return currentNeuron;
	}

}
