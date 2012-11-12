/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * Calculate the error between the current best value of the swarm and the
 * global optimum of the function.
 *
 */
public class ErrorMeasurement implements Measurement {

    private static final long serialVersionUID = 2632671785674388015L;
    private double target;

    public void setTarget(double value) {
        this.target = value;
    }

    @Override
    public Type getValue(Algorithm algorithm) {
        Problem problem = algorithm.getOptimisationProblem();
        double error = problem.getFitness(algorithm.getBestSolution().getPosition()).getValue() - target;
        return Real.valueOf(error);
    }

    @Override
    public Measurement getClone() {
        return this;
    }
}
