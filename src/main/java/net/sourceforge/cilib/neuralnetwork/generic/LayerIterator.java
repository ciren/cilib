/*
 * Created on 2005/04/03
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;

/**
 * @author stefanv
 *
 */
public interface LayerIterator {
	
	public void nextNeuron();
	
	public NeuronConfig value();
	
	public int getNrNeurons();
	
	public void reset();

	public boolean hasMore();
	
	public int currentPosition();
}
