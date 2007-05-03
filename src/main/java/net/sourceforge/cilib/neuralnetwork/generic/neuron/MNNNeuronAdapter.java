package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.StandardPattern;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;


//This class is not fully completed and tested.... Draft version
public class MNNNeuronAdapter extends NeuronConfig {
	
	GenericTopology topology;

	public Type computeActivationFunctionDerivativeAtPos(Type pos) {
		throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
	}

	public Type computeActivationFunctionDerivativeUsingLastOutput(Type lastOutput) {
		throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
	}

	public Type computeOutput(NeuronConfig n, NNPattern p) {
		
		MixedVector inputMNN = new MixedVector();
		MixedVector weightsMNN = new MixedVector();
		
		for (int i = 0; i < this.input.length; i++){
			MixedVector tmpInput = ((MixedVector)input[i].getCurrentOutput());
			MixedVector tmpWeight = ((MixedVector)inputWeights[i].getWeightValue());
			
			//copy relevant weights and inputs to vector
			for (int j = 0; j < tmpInput.size(); j ++){
				if (tmpWeight.get(j) != null){
					inputMNN.add(tmpInput.get(j));
					weightsMNN.add(tmpWeight.get(j));
				}
			}
		}//end for i

		//compute output
		NNPattern pat = new StandardPattern();
		pat.setInput(inputMNN);
		return this.topology.evaluate(pat);
	}

	public Type computeOutputFunctionDerivativeAtPos(Type pos) {
		throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
	}

	public Type computeOutputFunctionDerivativeUsingLastOutput(Type lastOutput) {
		throw new IllegalArgumentException("Method is unimplemented - please use a subclass.");
	}

}
