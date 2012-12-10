/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

import com.google.common.base.Predicate;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.util.Cloneable;

/**
 * A class that implements this interface can be used to measure the progress of
 *  an algorithm. Primarily, subclasses of this interface are used to determine
 * the stopping criteria for an {@link Algorithm}. Stopping conditions are
 * applied to algorithms using
 * {@link net.sourceforge.cilib.algorithm.Stoppable#addStoppingCondition(StoppingCondition)}.
 * <p>
 * Stopping conditions are also useful for implementing graphical progress bars and varying inertia
 * weights etc.
 */
public interface StoppingCondition<T extends Algorithm> extends Predicate<T>, Cloneable {

    /**
     * Determines the percentage complete for the associated algorithm.
     * @return the percentage completed as a fraction {@literal (0 <= i <= 1.0)}.
     */
    public double getPercentageCompleted(T algorithm);

    /**
     * {@inheritDoc}
     */
    @Override
    public StoppingCondition<T> getClone();
}
