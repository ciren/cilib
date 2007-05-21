package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;

public class VectorBasedFitnessCalculator implements FitnessCalculator {
	
	public VectorBasedFitnessCalculator() {
		
	}
	
	public VectorBasedFitnessCalculator(VectorBasedFitnessCalculator copy) {
		
	}
	
	public VectorBasedFitnessCalculator clone() {
		return new VectorBasedFitnessCalculator(this);
	}

	public Fitness getFitness(Type position) {
		Algorithm algorithm = Algorithm.get();
		return algorithm.getOptimisationProblem().getFitness(position, false);
	}

}
