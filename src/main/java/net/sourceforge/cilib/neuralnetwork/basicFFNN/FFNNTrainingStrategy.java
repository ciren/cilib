/*
 * Created on 2005/01/22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.basicFFNN;

import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;

/**
 * @author stefanv
 *
 */
public class FFNNTrainingStrategy implements TrainingStrategy{

	FFNNTopology topology = null;

	public FFNNTrainingStrategy(FFNNTopology topology) {
		super();
		this.topology = topology;
	}


	public void invokeTrainer(Object args) {
		topology.train();

	}


	public void preEpochActions(Object args) {
		//do nothing	
	}


	public void postEpochActions(Object args) {
		//do nothing	
	}


	public void initialize() {
		//do nothing		
	}


	public void setTopology(NeuralNetworkTopology topo) {
		//do nothing		
	}

}
