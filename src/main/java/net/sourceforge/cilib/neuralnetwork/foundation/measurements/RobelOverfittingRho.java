package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

public class RobelOverfittingRho implements Measurement {

	public String getDomain() {
		return "R";
	}

	public Type getValue() {
		NNError[] errorDt = ((NeuralNetworkProblem) ((NeuralNetworkController) Algorithm.get()).getOptimisationProblem()).getEvaluationStrategy().getErrorDt();
		NNError[] errorDg = ((NeuralNetworkProblem) ((NeuralNetworkController) Algorithm.get()).getOptimisationProblem()).getEvaluationStrategy().getErrorDg();
		
		Real rho = new Real();
		rho.setReal(errorDg[0].getValue().doubleValue() / errorDt[0].getValue().doubleValue());
		return rho;
	}

}
