/*
 * Created on 2005/04/18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author stefanv
 *
 */
public class LinearOutputFunction implements NeuronFunction {

	public LinearOutputFunction() {
		super();
	}
	
	
	public Type computeFunction(Type in) {
		return new Real(((Real)in).getReal());
	}

	
	public Type computeDerivativeAtPos(Type pos) {
		return new Real(1.0);
	}

	
	public Type computeDerivativeUsingLastOutput(Type lastOut) {
		return new Real(1.0);
	}

}
