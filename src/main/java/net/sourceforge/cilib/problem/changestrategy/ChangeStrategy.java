package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.problem.Problem;

/**
 * Interface detailing the potential operations that may be applied to the current problem.
 * 
 * This is particularly useful in Dynamic Environments when the need to alter the
 * problem search space.
 * 
 * It is possible to classify all problem instances as being dynamic problems. Problems that
 * remain unchanged are effectively problems where the applied change is a change that
 * preserves the problem search space, thus leaving it unchanged.
 */
public interface ChangeStrategy {

	/**
	 * Perform a change on the provided {@code problem} instance.
	 * @param problem The problem on which a change is to be applied.
	 */
	void change(Problem problem);

}
