/*
 * Created on 2005/03/21
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.type.types.Type;

/**
 * @author stefanv
 *
 * This is the interface that a Neuron output function needs to conform to
 */
public interface NeuronFunction {
	
	public Type computeFunction(Type in);
	
	public Type computeDerivativeAtPos(Type pos);
	
	public Type computeDerivativeUsingLastOutput(Type lastOut);

}
