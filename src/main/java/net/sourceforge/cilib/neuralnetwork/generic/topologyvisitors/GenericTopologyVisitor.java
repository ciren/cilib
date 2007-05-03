/*
 * Created on 2005/04/23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 * 
 */
public interface GenericTopologyVisitor {
	
	public void visitNeuronConfig(NeuronConfig n);

}
