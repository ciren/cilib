/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
 * @author gpampara
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
            problem.setFunction(new Spherical());

            PSO pso = new PSO();
            pso.setOptimisationProblem(problem);
            pso.addStoppingCondition(new MaximumIterations(1000));

            pso.initialise();
            pso.run();

            Assert.assertThat(pso.getBestSolution().getFitness().getValue(), is(400.5332366469983));
        }
        finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }

}
