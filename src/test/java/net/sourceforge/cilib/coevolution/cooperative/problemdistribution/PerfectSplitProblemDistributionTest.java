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
package net.sourceforge.cilib.coevolution.cooperative.problemdistribution;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PerfectSplitProblemDistributionTest {
    @Test
    public void PerfectSplitTest(){
        final DomainRegistry problemDomain = new StringBasedDomainRegistry();
        problemDomain.setDomainString("R(0.0, 4.0)^4");
        Bounds bounds = new Bounds(0.0, 4.0);
        Vector data = new Vector();
        data.add(Real.valueOf(0.0, bounds));
        data.add(Real.valueOf(0.0, bounds));
        data.add(Real.valueOf(0.0, bounds));
        data.add(Real.valueOf(0.0, bounds));

        List<PopulationBasedAlgorithm> populations = Arrays.asList((PopulationBasedAlgorithm)new PSO(), (PopulationBasedAlgorithm)new PSO());

        final OptimisationProblem problem = mock(OptimisationProblem.class);
        when(problem.getDomain()).thenReturn(problemDomain);

        PerfectSplitDistributionStrategy test = new PerfectSplitDistributionStrategy();
        test.performDistribution(populations, problem, data);

        CooperativeCoevolutionProblemAdapter p1 = (CooperativeCoevolutionProblemAdapter)populations.get(0).getOptimisationProblem();
        CooperativeCoevolutionProblemAdapter p2 = (CooperativeCoevolutionProblemAdapter)populations.get(1).getOptimisationProblem();

        assertEquals(2, p1.getDomain().getDimension(), 0.0);
        assertEquals(2, p2.getDomain().getDimension(), 0.0);

        assertEquals(0, p1.getProblemAllocation().getProblemIndex(0), 0.0);
        assertEquals(1, p1.getProblemAllocation().getProblemIndex(1), 0.0);

        assertEquals(2, p2.getProblemAllocation().getProblemIndex(0), 0.0);
        assertEquals(3, p2.getProblemAllocation().getProblemIndex(1), 0.0);
    }
}
