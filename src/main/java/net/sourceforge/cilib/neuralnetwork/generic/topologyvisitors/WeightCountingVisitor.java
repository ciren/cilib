/*
 * Created on 2005/08/14
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 */
public class WeightCountingVisitor implements GenericTopologyVisitor {

	private int weightCount;	

	public WeightCountingVisitor() {
		super();
		this.weightCount = 0;
	}


	public void visitNeuronConfig(NeuronConfig n) {
		if (n.getInputWeights() != null)
			weightCount += n.getInputWeights().length;
	}
	
	
	public int getWeightCount() {
		return weightCount;
	}

}
