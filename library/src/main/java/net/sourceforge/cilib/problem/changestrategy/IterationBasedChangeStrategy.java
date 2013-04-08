/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.problem.Problem;

/**
 * An {@code IterationBasedChangeStrategy} is a test to ensure that a problem is
 * altered or changed at a predefined frequency.
 * <p>
 * The default behaviour is to return {@code true} for all iterations. ie: the frequency
 * parameter {@code resolution} is set to have a value of {@code 1.0}.
 * </p>
 * @see ControlParameter
 *
 */
public class IterationBasedChangeStrategy implements ChangeStrategy {

    private ControlParameter resolution;

    /**
     * Create a new instance.
     */
    public IterationBasedChangeStrategy() {
        this.resolution = ConstantControlParameter.of(1.0);
    }

    /**
     * Determine if a change should be applied to the current problem, based
     * on the current number of iterations that have been completed.
     * @param problem The problem to query.
     * @return {@code true} if a change should be performed, {@code false} otherwise.
     */
    @Override
    public boolean shouldApply(Problem problem) {
        int iterations = AbstractAlgorithm.get().getIterations();
        return iterations != 0 && iterations % Double.valueOf(resolution.getParameter()).intValue() == 0;
    }

    public void setResolution(ControlParameter resolution) {
        this.resolution = resolution;
    }

    public ControlParameter getResolution() {
        return resolution;
    }

}
