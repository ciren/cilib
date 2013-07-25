/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

import fj.F;
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
public abstract class StoppingCondition extends F<Algorithm, Boolean> implements Cloneable {

    /**
     * Determines the percentage complete for the associated algorithm.
     * @return the percentage completed as a fraction {@literal (0 <= i <= 1.0)}.
     */
    public abstract double getPercentageCompleted(Algorithm algorithm);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract StoppingCondition getClone();

    public static final F<StoppingCondition, F<Algorithm, Boolean>> toFunc = new F<StoppingCondition, F<Algorithm, Boolean>>() {
        @Override
        public F<Algorithm, Boolean> f(final StoppingCondition s) {
            return new F<Algorithm, Boolean>() {
                @Override
                public Boolean f(Algorithm a) {
                    return s.f(a);
                }
            };
        }
    };
}
