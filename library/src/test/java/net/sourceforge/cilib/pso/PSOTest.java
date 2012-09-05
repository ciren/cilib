/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
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
