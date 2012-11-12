/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.ec.iterationstrategies.DifferentialEvolutionIterationStrategy;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ECTest {

    @Test
    public void algorithmTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
            problem.setDomain("R(-5.12:5.12)^30");
            problem.setFunction(new Spherical());

            EC ec = new EC();
            ec.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10));
            ec.setOptimisationProblem(problem);
            ec.performInitialisation();
            ec.run();

            //Assert.assertThat(ec.getBestSolution().getFitness().getValue(), is(247.1330178942291));
            Assert.assertThat(ec.getBestSolution().getFitness().getValue(), is(268.60354620427955));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }

    @Test
    public void deTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
            problem.setDomain("R(-5.12:5.12)^30");
            problem.setFunction(new Spherical());

            EC ec = new EC();
            ec.setIterationStrategy(new DifferentialEvolutionIterationStrategy());
            ec.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10));
            ec.setOptimisationProblem(problem);
            ec.performInitialisation();
            ec.run();

            Assert.assertThat(ec.getBestSolution().getFitness().getValue(), is(400.5332366469983));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
}
