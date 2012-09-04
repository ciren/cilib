/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ShuffleProblemRedistributionTest {

    @Test
    public void ShuffleProblemTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());
        try {
            final OptimisationProblem problem1 = mock(OptimisationProblem.class);
            final OptimisationProblem problem2 = mock(OptimisationProblem.class, "optimisationProblem2");
            final OptimisationProblem problem3 = mock(OptimisationProblem.class, "optimisationProblem3");

            when(problem1.getClone()).thenReturn(problem1);
            when(problem2.getClone()).thenReturn(problem2);
            when(problem3.getClone()).thenReturn(problem3);

            final PopulationBasedAlgorithm testAlgorithm1 = mock(PopulationBasedAlgorithm.class);
            final PopulationBasedAlgorithm testAlgorithm2 = mock(PopulationBasedAlgorithm.class, "populationBasedAlgorithm2");
            final PopulationBasedAlgorithm testAlgorithm3 = mock(PopulationBasedAlgorithm.class, "populationBasedAlgorithm3");
            
            when(testAlgorithm1.getOptimisationProblem()).thenReturn(problem1);
            when(testAlgorithm2.getOptimisationProblem()).thenReturn(problem2);
            when(testAlgorithm3.getOptimisationProblem()).thenReturn(problem3);

            final List<PopulationBasedAlgorithm> populations = new ArrayList<PopulationBasedAlgorithm>();
            populations.add(testAlgorithm1);
            populations.add(testAlgorithm2);
            populations.add(testAlgorithm3);

            final Vector contextEntity = mock(Vector.class);

            final ProblemDistributionStrategy distribution = mock(ProblemDistributionStrategy.class);

            ShuffleProblemRedistributionStrategy shuffleTest = new ShuffleProblemRedistributionStrategy();
            shuffleTest.redistributeProblem(populations, problem1, distribution, contextEntity);
            
            verify(testAlgorithm3).setOptimisationProblem(problem1);
            verify(testAlgorithm1).setOptimisationProblem(problem2);
            verify(testAlgorithm2).setOptimisationProblem(problem3);

        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
}
