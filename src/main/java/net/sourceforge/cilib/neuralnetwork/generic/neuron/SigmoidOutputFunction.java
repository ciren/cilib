/*
 * Created on 2005/03/21
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
public class SigmoidOutputFunction implements NeuronFunction{
	
	double lambda;
	
	
	public SigmoidOutputFunction() {
		lambda = 1;
	}
		
	
	
	public Type computeFunction(Type in) {
		return new Real(1.0 / (1.0 + Math.exp(-1.0 *lambda * ((Real)in).getReal())));
	}


	public Type computeDerivativeAtPos(Type pos) {
		return new Real(((Real)computeFunction(pos)).getReal() * (1 - ((Real)computeFunction(pos)).getReal()));
	}


	public Type computeDerivativeUsingLastOutput(Type lastOut) {
		return new Real( ((Real)lastOut).getReal() * (1 - ((Real)lastOut).getReal()) );
	}

}
