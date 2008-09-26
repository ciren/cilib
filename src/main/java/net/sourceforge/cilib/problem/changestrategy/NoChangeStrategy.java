package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.problem.Problem;

/**
 * Environmental change strategy for problems that does nothing.
 */
public class NoChangeStrategy implements ChangeStrategy {

	/**
	 * Do not change the search space of the provided problem instance. Leave
	 * the search space intact.
	 * <p>
	 * This method simply returns and performs no actions.
	 */
	public void change(Problem problem) {
		// This is intentionally left empty
		// For Theuns: Living Laxly Like A Lazy Lounge Lizzard
	}

}
