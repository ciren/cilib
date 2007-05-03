package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.UnimplementedMethodException;

public class DcPatternCount implements Measurement {
	private static final long serialVersionUID = -5891911509569750125L;

	public DcPatternCount() {
	}

	public DcPatternCount(DcPatternCount rhs) {
//		super(rhs);
		throw new UnimplementedMethodException("public DcPatternCount(DcPatternCount rhs)");
	}

	public DcPatternCount clone() {
		return new DcPatternCount(this);
	}

	public String getDomain() {
		return "Z";
	}

	public Type getValue() {
		int size = ((NeuralNetworkProblem) ((NeuralNetworkController) Algorithm.get()).getOptimisationProblem()).getEvaluationStrategy().getData().getCandidateSetSize();
		return new Int(size);
	}
}
