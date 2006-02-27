/*
 * LinearOutputFunction.java
 * 
 * Created on Apr 18, 2004
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
public class LinearOutputFunction implements NeuronFunction {

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronFunction#computeFunction(java.lang.Object)
	 */
	public Object computeFunction(Object in) {
		return new Double(((Double)in).doubleValue());
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronFunction#computeDerivativeAtPos(java.lang.Object)
	 */
	public Object computeDerivativeAtPos(Object pos) {
		return new Double(1);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronFunction#computeDerivativeUsingLastOutput(java.lang.Object)
	 */
	public Object computeDerivativeUsingLastOutput(Object lastOut) {
		return new Double(1);
	}

}
