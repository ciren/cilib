package net.sourceforge.cilib.util.calculator;

import java.io.Serializable;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;

/**
 * Perform the calculation of the fitness for the given <code>Entity</code>, decoupling the
 * <code>Entity</code> from the <code>Problem</code>.
 */
public interface FitnessCalculator extends Serializable {
	
	/**
	 * Clone the current <code>FitnessCalculator</code>
	 * @return A new instance of the current <code>FitnessCalculator</code>
	 */
	public FitnessCalculator clone();

	/**
	 * Get the fitness, given the <code>position</code>
	 * @param position The <code>Type</code> to base the calculation on
	 * @param count Whether or not the evaluation is to be counted
	 * @return A <code>Fitness</code> object representing the fitness of the <code>position</code>
	 */
	public Fitness getFitness(Type position, boolean count);

}
