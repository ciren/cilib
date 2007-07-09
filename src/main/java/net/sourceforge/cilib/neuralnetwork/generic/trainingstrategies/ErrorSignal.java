/*
 * Created on 2005/04/18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.trainingstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.MixedVector;

/**
 * @author stefanv
 *
 */
public interface ErrorSignal {
	
	public Type computeBaseDelta(Type desired, Type output, Type outputFunctionDerivative);
	
	public Type computeRecursiveDelta(Type outputFunctionDerivative, 
			MixedVector delta, 
			ArrayList<Weight> w, 
			Type output);

}
