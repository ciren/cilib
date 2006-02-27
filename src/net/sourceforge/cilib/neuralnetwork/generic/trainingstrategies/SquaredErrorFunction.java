/*
 * SquaredErrorFunction.java
 * 
 * Created on Apr 18, 2005
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
package net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SquaredErrorFunction implements ErrorSignal {

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.TrainingStrategies.ErrorSignal#computeBaseDelta(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public Object computeBaseDelta(Object desired, Object output,
			Object outputFunctionDerivative) {
		
		/*System.out.println("Base delta = " + -1 * ((Double)desired - (Double)output) * (Double)outputFunctionDerivative);
		System.out.println("Desired = " + ((Double)desired).doubleValue());
		System.out.println("output = " + ((Double)output).doubleValue());
		System.out.println("output Derivative = " + ((Double)outputFunctionDerivative).doubleValue());
		*/
		Double res = new Double(-1 * ((Double)desired - (Double)output) * (Double)outputFunctionDerivative);
		return res;
		
		//return new Double(1);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Generic.TrainingStrategies.ErrorSignal#computeRecursiveDelta(java.lang.Object, java.util.ArrayList, java.util.ArrayList, java.lang.Object)
	 */
	public Object computeRecursiveDelta(Object outputFunctionDerivative,
			ArrayList delta, ArrayList<Weight> w, Object output) {
		
		
		
		Double sumResult = new Double(0);
		for (int i = 0; i < w.size(); i++){
			sumResult += (Double)delta.get(i) * (Double)w.get(i).getWeightValue();
		}
		
		
		/*System.out.println("---output = " + ((Double)output).doubleValue());
		System.out.println("derivative = " + ((Double)outputFunctionDerivative).doubleValue());
		System.out.println("RecursveDelta = " + ((Double)outputFunctionDerivative * sumResult));		
		
		System.out.println("delta[0] = " + ((Double)delta.get(0)).doubleValue());
		System.out.println("weight = " + ((Double)w.get(0).getWeightValue()).doubleValue());
		*/
		
		
		return ((Double)outputFunctionDerivative * sumResult);  
		
		//return new Double(1);
	}

}
