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
 * Signal a change periodically based on the number of iteration.
 * At most one change will be signalled per iteration by an instance of this class.
 */
public class IterationBasedSingleChangeStrategy implements ChangeStrategy {

    private ControlParameter resolution;
    private int changeCounter;

    public IterationBasedSingleChangeStrategy() {
        this(100);
    }

    public IterationBasedSingleChangeStrategy(int numberOfIterationBetweenChanges) {
        this.resolution = ConstantControlParameter.of(numberOfIterationBetweenChanges);
        this.changeCounter = 0;
    }

    @Override
    public boolean shouldApply(Problem problem) {
        int iterations = AbstractAlgorithm.get().getIterations();
        if (iterations != 0 && iterations % Double.valueOf(resolution.getParameter()).intValue() == 0 && iterations!=changeCounter){
            changeCounter = iterations;
            return true;
        }

        return false;
    }

    public void setResolution(ControlParameter resolution) {
        this.resolution = resolution;
    }

    public ControlParameter getResolution() {
        return resolution;
    }

}
