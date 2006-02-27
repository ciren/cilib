/*
 * DotProductPipeline.java
 * 
 * Created on Mar 23, 2005
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
public class DotProductPipeline implements NeuronPipeline {

	NeuronFunction outputFunction = null;
		
	
	/**
	 * @param outputFunction_
	 */
	public DotProductPipeline(NeuronFunction outputFunction_) {
		super();
		this.outputFunction = outputFunction_;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#compute(net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronConfig, net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern)
	 */
	public Object computeOutput(NeuronConfig n, NNPattern p) {
		
		//Compute the activation value
		double activation = 0;
				
		if (n.getPatternInputPos() >= 0){
		
			//TODO: Invetigate template NNpatterns and weights, convert to Type operations...
			
			activation = ((Double) p.getInput().get(n.getPatternInputPos())).doubleValue() *
						 ((Double) n.getPatternWeight().getWeightValue()).doubleValue();
			//System.out.println("PATTERN INPUT: " + n.getPatternInputPos() + " value = " + ((Double)p.getInput().get(n.getPatternInputPos())).doubleValue() + ", activation = " + activation);
		}
		
		// then
		if (n.getInput() != null) {
			for (int i = 0; i < n.getInput().length; i++) {
				//System.out.println("Dotprodcut pipe: input neuron " + i + "in computeOutput" );
				double output;

				if (n.getTimeStepMap()[i] == false)
					output = ((Double) n.getInput()[i].getCurrentOutput()).doubleValue();
				else
					output = ((Double) n.getInput()[i].getTminus1Output()).doubleValue();
				
			//	System.out.print("\t input " + i + " from neuron = " + output );
			//	System.out.println("Weight = "+ ((Double) n.getInputWeights()[i].getWeightValue()).doubleValue());

				activation += output * ((Double) n.getInputWeights()[i].getWeightValue())
								.doubleValue();

			} //end for i

		}
		
		
		//Compute the output value
		//System.out.println("Dotproduct Pipeline: activation net input: " + activation);
		
		Double output = (Double)outputFunction.computeFunction(activation);
				
		return output;

	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeOutputFunctionDerivativeAtPos(java.lang.Object)
	 */
	public Object computeOutputFunctionDerivativeAtPos(Object pos) {
		return outputFunction.computeDerivativeAtPos(pos);
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeOutputFunctionDerivativeUsingLastOutput(java.lang.Object)
	 */
	public Object computeOutputFunctionDerivativeUsingLastOutput(Object lastOutput) {
		return outputFunction.computeDerivativeUsingLastOutput(lastOutput);
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeActivationFunctionDerivativeAtPos(java.lang.Object)
	 */
	public Object computeActivationFunctionDerivativeAtPos(Object pos) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.Neuron.NeuronPipeline#computeActivationFunctionDerivativeUsingLastOutput(java.lang.Object)
	 */
	public Object computeActivationFunctionDerivativeUsingLastOutput(Object lastOutput) {
		// TODO Auto-generated method stub
		return null;
	}

}
