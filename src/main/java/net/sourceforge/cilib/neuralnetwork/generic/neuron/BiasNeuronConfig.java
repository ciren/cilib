package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

public class BiasNeuronConfig extends NeuronConfig {

	public BiasNeuronConfig() {
		super();
	}

	public BiasNeuronConfig(int pipeIndex, Type initValC, Type initValT) {
		super(pipeIndex, initValC, initValT);
	}

	public BiasNeuronConfig(NeuronConfig[] input, 
							Weight[] inputWeights,
							boolean[] timeStepMap, 
							int patternInput, 
							Weight patternWeight,
							Type initialOutput) {
		
		super(input, inputWeights, timeStepMap, patternInput, patternWeight,
				initialOutput);
	}
	
	
	public Type computeOutput(NeuronConfig n, NNPattern p) {
		return new Real(-1);
	}

	
	public Type computeOutputFunctionDerivativeAtPos(Type pos) {
		return null;
	}

	
	public Type computeOutputFunctionDerivativeUsingLastOutput(
			Type lastOutput) {
		return null;
	}

	
	public Type computeActivationFunctionDerivativeAtPos(Type pos) {
		return null;
	}

	
	public Type computeActivationFunctionDerivativeUsingLastOutput(
			Type lastOutput) {
		return null;
	}

}
