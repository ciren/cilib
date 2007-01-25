/*
 * SigmoidOutputFunction.java
 * 
 * Created on Mar 03, 2005
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
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SigmoidOutputFunction implements NeuronFunction{
	
	double lambda = 1;
	
	/**
	 * 
	 */
	public SigmoidOutputFunction() {
		lambda = 1;
	}
	
	
	
	/**
	 * @param lambda
	 */
	public SigmoidOutputFunction(double lambda) {
		super();
		this.lambda = lambda;
	}
	
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.outputFunction#compute(java.lang.Object)
	 */
	public Object computeFunction(Object in) {
		//TODO: gamma learning
		return new Double(1.0 / (1.0 + Math.exp(-1.0 *lambda * ((Double)in).doubleValue())));
	}



	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronFunction#computeDerivativeAtPos(java.lang.Object)
	 */
	public Object computeDerivativeAtPos(Object pos) {
		Double result = (Double)computeFunction(pos) * (new Double(1.0) - ((Double)computeFunction(pos)));
		return result;
	}



	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronFunction#computeDerivativeUsingLastOutput(java.lang.Object)
	 */
	public Object computeDerivativeUsingLastOutput(Object lastOut) {
		return new Double((Double)lastOut * (new Double(1.0) - (Double)lastOut) );
	}

}
