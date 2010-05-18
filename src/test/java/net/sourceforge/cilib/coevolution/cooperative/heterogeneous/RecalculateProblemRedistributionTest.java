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
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
public class RecalculateProblemRedistributionTest {

    private Mockery context = new Mockery()     {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @Test
    public void RecalculateRedistributionTest(){
        final List<PopulationBasedAlgorithm> populations = new ArrayList<PopulationBasedAlgorithm>();
        final OptimisationProblem problem = context.mock(OptimisationProblem.class);
        final Vector contextEntity = context.mock(Vector.class);

        RecalculateProblemRedistributionStrategy recalcStrategy = new RecalculateProblemRedistributionStrategy();

         final ProblemDistributionStrategy distribution = context.mock(ProblemDistributionStrategy.class);
         context.checking(new Expectations() {{ //make sure this method is called
             atLeast(1).of (distribution).performDistribution(populations, problem, contextEntity);
            }});

        recalcStrategy.redistributeProblem(populations, problem, distribution, contextEntity);

        context.assertIsSatisfied(); //assert that alll the required methods have been invoked
    }
}
