/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.problem.Problem;

/**
 * Interface used to determine if change operations should be applied to the current problem.
 * <p>
 * This is particularly useful in Dynamic Environments when the need to alter the
 * problem search space.
 * </p>
 * <p>
 * It is possible to classify all problem instances as being dynamic problems. Problems that
 * remain unchanged are effectively problems where the applied change is a change that
 * preserves the problem search space, thus leaving it unchanged.
 * </p>
 */
public interface ChangeStrategy {

    /**
     * Determine whether a change should be applied to the provided {@code problem} instance.
     * @param problem The problem on which a change is to be applied.
     * @return {@code true} if a change occurred, {@code false} otherwise.
     */
    boolean shouldApply(Problem problem);

}
