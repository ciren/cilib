package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

public class DgPatternCount implements Measurement {
	private static final long serialVersionUID = 1660067769897420258L;

	public DgPatternCount() {
	}

	public DgPatternCount(DgPatternCount rhs) {
//		super(rhs);
		throw new UnsupportedOperationException("public DgPatternCount(DgPatternCount rhs)");
	}

	public DgPatternCount getClone() {
		return new DgPatternCount(this);
	}

	public String getDomain() {
		return "Z";
	}

	public Type getValue() {
		int size = ((NeuralNetworkProblem) ((NeuralNetworkController) Algorithm.get()).getOptimisationProblem()).getEvaluationStrategy().getData().getGeneralisationSetSize();
		return new Int(size);
	}
}
