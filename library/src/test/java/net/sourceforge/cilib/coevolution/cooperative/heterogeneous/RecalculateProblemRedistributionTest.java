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
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class RecalculateProblemRedistributionTest {

    @Test
    public void RecalculateRedistributionTest() {
        final List<PopulationBasedAlgorithm> populations = new ArrayList<PopulationBasedAlgorithm>();
        final Problem problem = mock(Problem.class);
        final Vector contextEntity = mock(Vector.class);

        RecalculateProblemRedistributionStrategy recalcStrategy = new RecalculateProblemRedistributionStrategy();

        final ProblemDistributionStrategy distribution = mock(ProblemDistributionStrategy.class);

        recalcStrategy.redistributeProblem(populations, problem, distribution, contextEntity);

        verify(distribution, atLeast(1)).performDistribution(populations, problem, contextEntity);
    }
}
