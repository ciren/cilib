/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.problem.Problem;

/**
 * Environmental change strategy for problems that does not allow
 * a change to occur.
 */
public class NoChangeStrategy implements ChangeStrategy {

    /**
     * Do not change the search space of the provided problem instance. Leave
     * the search space intact.
     * <p>
     * This method simply returns {@code false} and performs no actions.
     * @param problem The problem to be queried for change.
     * @return {@code false} always.
     */
    @Override
    public boolean shouldApply(Problem problem) {
        return false;
        // For Theuns: Living Laxly Like A Lazy Lounge Lizzard
    }

}
