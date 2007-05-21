package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;

public interface FitnessCalculator {
	
	public FitnessCalculator clone();

	public Fitness getFitness(Type bestPosition);

}
