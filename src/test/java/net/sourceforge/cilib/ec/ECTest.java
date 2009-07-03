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
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.ec.iterationstrategies.DifferentialEvolutionIterationStrategy;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author gpampara
 */
public class ECTest {

    @Test
    public void algorithmTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            EC ec = new EC();

            ec.addStoppingCondition(new MaximumIterations(10));

            FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
            problem.setFunction(new Spherical());

            ec.setOptimisationProblem(problem);

            ec.initialise();
            ec.run();

            Assert.assertThat(ec.getBestSolution().getFitness().getValue(), is(270.0592679172665));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }

    @Test
    public void deTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            EC ec = new EC();
            ec.setIterationStrategy(new DifferentialEvolutionIterationStrategy());

            ec.addStoppingCondition(new MaximumIterations(10));

            FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
            problem.setFunction(new Spherical());

            ec.setOptimisationProblem(problem);

            ec.initialise();
            ec.run();

            Assert.assertThat(ec.getBestSolution().getFitness().getValue(), is(400.5332366469983));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }

}
