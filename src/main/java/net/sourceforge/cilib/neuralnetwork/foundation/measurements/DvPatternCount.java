package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

public class DvPatternCount implements Measurement {
	private static final long serialVersionUID = -5428882385996496716L;

	public DvPatternCount() {
	}

	public DvPatternCount(DvPatternCount rhs) {
//		super(rhs);
		throw new UnsupportedOperationException("public DvPatternCount(DvPatternCount rhs)");
	}

	public DvPatternCount clone() {
		return new DvPatternCount(this);
	}

	public String getDomain() {
		return "Z";
	}

	public Type getValue() {
		int size = ((NeuralNetworkProblem) ((NeuralNetworkController) Algorithm.get()).getOptimisationProblem()).getEvaluationStrategy().getData().getValidationSetSize();
		return new Int(size);
	}
}
