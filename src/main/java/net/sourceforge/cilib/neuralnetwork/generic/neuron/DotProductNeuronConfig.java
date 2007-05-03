package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

public class DotProductNeuronConfig extends NeuronConfig {

	NeuronFunction outputFunction = null;

	public DotProductNeuronConfig() {
		super();
	}

	public DotProductNeuronConfig(int pipeIndex, Type initValC, Type initValT) {
		super(pipeIndex, initValC, initValT);
	}

	public DotProductNeuronConfig(NeuronConfig[] input, 
			Weight[] inputWeights,
			boolean[] timeStepMap, 
			int patternInput, 
			Weight patternWeight,
			Type initialOutput) {

		super(input, inputWeights, timeStepMap, patternInput, patternWeight,
				initialOutput);

	}

	public Type computeOutput(NeuronConfig n, NNPattern p) {

		//Compute the activation value
		double activation = 0;

		if (n.getPatternInputPos() >= 0){

			activation = ((Real) p.getInput().get(n.getPatternInputPos())).getReal() *
			((Real) n.getPatternWeight().getWeightValue()).getReal();

		}

		// then
		if (n.getInput() != null) {
			for (int i = 0; i < n.getInput().length; i++) {
				double output;

				if (n.getTimeStepMap()[i] == false)
					output = ((Real) n.getInput()[i].getCurrentOutput()).getReal();
				else
					output = ((Real) n.getInput()[i].getTminus1Output()).getReal();

				activation += output * ((Real) n.getInputWeights()[i].getWeightValue()).getReal();

			} //end for i

		}


		//Compute the output value
		//System.out.println("Dotproduct Pipeline: activation net input: " + activation);

		Type output = outputFunction.computeFunction(new Real(activation));

		return output;

	}



	public Type computeOutputFunctionDerivativeAtPos(Type pos) {
		return outputFunction.computeDerivativeAtPos(pos);
	}


	public Type computeOutputFunctionDerivativeUsingLastOutput(Type lastOutput) {
		return outputFunction.computeDerivativeUsingLastOutput(lastOutput);
	}


	public Type computeActivationFunctionDerivativeAtPos(Type pos) {
		return null;
	}



	public Type computeActivationFunctionDerivativeUsingLastOutput(Type lastOutput) {
		return null;
	}

	public NeuronFunction getOutputFunction() {
		return outputFunction;
	}

	public void setOutputFunction(NeuronFunction outputFunction) {
		this.outputFunction = outputFunction;
	}


}
