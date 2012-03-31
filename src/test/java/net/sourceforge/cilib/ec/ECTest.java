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
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.ec.iterationstrategies.DifferentialEvolutionIterationStrategy;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
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
            FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
            problem.setDomain("R(-5.12, 5.12)^30");
            problem.setFunction(new Spherical());

            EC ec = new EC();
            ec.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10));
            ec.setOptimisationProblem(problem);
            ec.initialise();
            ec.run();

            Assert.assertThat(ec.getBestSolution().getFitness().getValue(), is(247.1330178942291));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }

    @Test
    public void deTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
            problem.setDomain("R(-5.12, 5.12)^30");
            problem.setFunction(new Spherical());

            EC ec = new EC();
            ec.setIterationStrategy(new DifferentialEvolutionIterationStrategy());
            ec.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10));
            ec.setOptimisationProblem(problem);
            ec.initialise();
            ec.run();

            Assert.assertThat(ec.getBestSolution().getFitness().getValue(), is(400.5332366469983));
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
}
