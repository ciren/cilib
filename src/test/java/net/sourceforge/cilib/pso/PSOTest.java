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

import static org.hamcrest.CoreMatchers.is;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;

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
            FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
            problem.setDomain("R(-5.12, 5.12)^30");
            problem.setFunction(new Spherical());

            PSO pso = new PSO();
            pso.setOptimisationProblem(problem);
            pso.addStoppingCondition(new MaximumIterations(1000));

            pso.initialise();
            pso.run();

            Assert.assertThat(pso.getBestSolution().getFitness().getValue(), is(400.5332366469983));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
}
