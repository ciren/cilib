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
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
public class ShuffleProblemRedistributionTest {

    private Mockery context = new Mockery()     {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @Test
    public void ShuffleProblemTest(){

        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());
        try {
            final OptimisationProblem problem1 = context.mock(OptimisationProblem.class);
            context.checking(new Expectations() {{
                atLeast(1).of (problem1).getClone(); will(returnValue(problem1));
                }});
            final OptimisationProblem problem2 = context.mock(OptimisationProblem.class, "optimisationProblem2");
            context.checking(new Expectations() {{
                atLeast(1).of (problem2).getClone(); will(returnValue(problem2));
                }});
            final OptimisationProblem problem3 = context.mock(OptimisationProblem.class, "optimisationProblem3");
            context.checking(new Expectations() {{
                atLeast(1).of (problem3).getClone(); will(returnValue(problem3));
                }});

        //    final Sequence setProblemSequence = context.sequence("setProblemSequence");

            final PopulationBasedAlgorithm testAlgorithm1 = context.mock(PopulationBasedAlgorithm.class);
            context.checking(new Expectations() {{
                 oneOf(testAlgorithm1).getOptimisationProblem(); will(returnValue(problem1));
                 oneOf (testAlgorithm1).setOptimisationProblem(problem2);
                }});

            final PopulationBasedAlgorithm testAlgorithm2 = context.mock(PopulationBasedAlgorithm.class, "populationBasedAlgorithm2");
            context.checking(new Expectations() {{
                 oneOf(testAlgorithm2).getOptimisationProblem(); will(returnValue(problem2));
                 oneOf (testAlgorithm2).setOptimisationProblem(problem3);
                }});

            final PopulationBasedAlgorithm testAlgorithm3 = context.mock(PopulationBasedAlgorithm.class, "populationBasedAlgorithm3");
            context.checking(new Expectations() {{
                 oneOf(testAlgorithm3).getOptimisationProblem(); will(returnValue(problem3));
                 oneOf (testAlgorithm3).setOptimisationProblem(problem1);
                }});

            final List<PopulationBasedAlgorithm> populations = new ArrayList<PopulationBasedAlgorithm>();
            populations.add(testAlgorithm1);
            populations.add(testAlgorithm2);
            populations.add(testAlgorithm3);

            final Vector contextEntity = context.mock(Vector.class);

            final ProblemDistributionStrategy distribution = context.mock(ProblemDistributionStrategy.class);

            ShuffleProblemRedistributionStrategy shuffleTest = new ShuffleProblemRedistributionStrategy();
            shuffleTest.redistributeProblem(populations, problem1, distribution, contextEntity);

            context.assertIsSatisfied(); //assert that alll the required methods have been invoked
        }
        finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
}
