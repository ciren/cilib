/*
 * BiasNeuronPipeline.java
 * 
 * Created on Apr 17, 2005
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

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BiasNeuronPipeline implements NeuronPipeline {

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeOutput(net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronConfig, net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern)
	 */
	public Object computeOutput(NeuronConfig n, NNPattern p) {
		return new Double(-1);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeOutputFunctionDerivativeAtPos(java.lang.Object)
	 */
	public Object computeOutputFunctionDerivativeAtPos(Object pos) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeOutputFunctionDerivativeUsingLastOutput(java.lang.Object)
	 */
	public Object computeOutputFunctionDerivativeUsingLastOutput(
			Object lastOutput) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeActivationFunctionDerivativeAtPos(java.lang.Object)
	 */
	public Object computeActivationFunctionDerivativeAtPos(Object pos) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeActivationFunctionDerivativeUsingLastOutput(java.lang.Object)
	 */
	public Object computeActivationFunctionDerivativeUsingLastOutput(
			Object lastOutput) {
		return null;
	}

}
