/*
 * Created on 2005/04/18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author stefanv
 *
 */
public class SquaredErrorFunction implements ErrorSignal {
	
	public SquaredErrorFunction() {
		super();
		
	}

	
	public Type computeBaseDelta(Type desired, Type output,
			Type outputFunctionDerivative) {
		
		return new Real(-1 * ( ((Real)desired).getReal() - ((Real)output).getReal()) * ((Real)outputFunctionDerivative).getReal());
				
	}


	public Type computeRecursiveDelta(Type outputFunctionDerivative,
			Vector delta, ArrayList<Weight> w, Type output) {
		
		
		
		double sumResult = 0;
		for (int i = 0; i < w.size(); i++){
			sumResult += ((Real)delta.get(i)).getReal() * ((Real)w.get(i).getWeightValue()).getReal();
		}
		
	
		return new Real( ((Real)outputFunctionDerivative).getReal() * sumResult);  
		
	}

}
