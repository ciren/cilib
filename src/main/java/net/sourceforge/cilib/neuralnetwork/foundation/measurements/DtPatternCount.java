package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;

public class DtPatternCount implements Measurement {
	private static final long serialVersionUID = -7985184255274982621L;

	public DtPatternCount() {
	}

	public DtPatternCount(DtPatternCount rhs) {
//		super(rhs);
		throw new UnsupportedOperationException("public DtPatternCount(DtPatternCount rhs)");
	}

	public DtPatternCount getClone() {
		return new DtPatternCount(this);
	}

	public String getDomain() {
		return "Z";
	}

	public Type getValue() {
		int size = ((NeuralNetworkProblem) ((NeuralNetworkController) Algorithm.get()).getOptimisationProblem()).getEvaluationStrategy().getData().getTrainingSetSize();
		return new Int(size);
	}
}
