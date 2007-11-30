package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;

public class VectorBasedFitnessCalculator implements FitnessCalculator {
	private static final long serialVersionUID = -5053760817332028741L;

	public VectorBasedFitnessCalculator() {
		
	}
	
	public VectorBasedFitnessCalculator(VectorBasedFitnessCalculator copy) {
		
	}
	
	public VectorBasedFitnessCalculator getClone() {
		return new VectorBasedFitnessCalculator(this);
	}

	public Fitness getFitness(Type position, boolean count) {
		Algorithm algorithm = Algorithm.get();
		return algorithm.getOptimisationProblem().getFitness(position, count);
	}

}
