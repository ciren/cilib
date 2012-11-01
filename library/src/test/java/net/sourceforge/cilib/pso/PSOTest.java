/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso;

import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PSOTest {

    /**
     * <p>
     * This test ensures that the general functioning of the PSO is correct.
     * the correctness is enforced by ensuring that the algorithm is no longer
     * stochastic by forcing all the random numbers to have the same seed. This
     * results in a very deterministic algorithm.
     * </p>
     * <p>
     * The PSO is tested using the following defaults:
     * <ul>
     *   <li>Function minimization of the Spherical function.</li>
     *   <li>1000 iterations.</li>
     *   <li>GBest topology with 20 particles.</li>
     * </ul>
     * </p>
     */
    @Test
    public void algorithmExecution() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
            problem.setDomain("R(-5.12:5.12)^30");
            problem.setFunction(new Spherical());

            PSO pso = new PSO();
            pso.setOptimisationProblem(problem);
            pso.addStoppingCondition(new MeasuredStoppingCondition());

            pso.performInitialisation();
            pso.run();

            Assert.assertThat(pso.getBestSolution().getFitness().getValue(), is(400.5332366469983));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
}
