/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

import com.google.common.base.Predicate;
import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * <p>
 * A class that implements this interface can be used to measure the progress of an algorithm.
 * Primarily, subclasses of this interface are used to determine the stopping criteria for an
 * {@link net.sourceforge.cilib.algorithm.Algorithm}. Stopping conditions are applied to algorithms
 * using {@link Algorithm#addStoppingCondition(StoppingCondition)}.
 * </p>
 * <p>
 * Stopping conditions are also useful for implementing graphical progress bars and varying inertia
 * weights etc.
 * </p>
 */
public interface StoppingCondition<T extends Algorithm> extends Predicate<T> {

    /**
     * Determines the percentage complete for the associated algorithm.
     * @returns The percentage completed as a fraction {@literal (0 <= i <= 1.0)}.
     */
    public double getPercentageCompleted(T algorithm);
}
