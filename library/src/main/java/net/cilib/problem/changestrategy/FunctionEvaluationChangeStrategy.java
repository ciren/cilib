/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.problem.changestrategy;

import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.controlparameter.ControlParameter;
import net.cilib.problem.Problem;

public class FunctionEvaluationChangeStrategy implements ChangeStrategy {

    private ControlParameter resolution;

    public FunctionEvaluationChangeStrategy() {
        resolution = ConstantControlParameter.of(1e5);
    }

    public boolean shouldApply(Problem problem) {
        return problem.getFitnessEvaluations() % (int) resolution.getParameter() == 0;
    }

    public void setResolution(ControlParameter resolution) {
        this.resolution = resolution;
    }

    public ControlParameter getResolution() {
        return resolution;
    }

}
