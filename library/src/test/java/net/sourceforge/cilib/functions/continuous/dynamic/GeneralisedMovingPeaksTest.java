/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.functions.continuous.dynamic;

import net.cilib.measurement.generic.Iterations;
import net.cilib.problem.FunctionOptimisationProblem;
import net.cilib.problem.objective.Maximise;
import net.cilib.pso.PSO;
import net.cilib.stoppingcondition.Maximum;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Test;

/**
 *
 */
public class GeneralisedMovingPeaksTest {

    @Test
    public void generalizedMovingPeaks() {
        int frequency = 1;
        int peaks = 3;
        double widthSeverity = 0.1;
        double heightSeverity = 3.0;
        double shiftSeverity = 2;
        double lambda = 1.0;

        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setObjective(new Maximise());
        problem.setDomain("R(0.0:100.0)^2");
        problem.setFunction(new GeneralisedMovingPeaks(frequency, peaks, widthSeverity, heightSeverity, shiftSeverity, lambda));

        PSO pso = new PSO();
        pso.setOptimisationProblem(problem);
        pso.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 100));

        //pso.initialise();
        pso.run();
    }
}
