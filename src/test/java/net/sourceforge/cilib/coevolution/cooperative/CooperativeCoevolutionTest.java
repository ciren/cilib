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
package net.sourceforge.cilib.coevolution.cooperative;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contextupdate.SelectiveContextUpdateStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequencialDimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CooperativeCoevolutionTest {

    @Test
    public void CoevolutionTest() {

        //fake result vectors for the two sub-population algorithms
        final Vector pop1Result = new Vector();
        pop1Result.add(Real.valueOf(1));
        final Vector pop2Result = new Vector();
        pop2Result.add(Real.valueOf(2));
        //fake random vectors for the two subpopulation algorithms
        final Vector pop1Rand = new Vector();
        pop1Rand.add(Real.valueOf(4));
        final Vector pop2Rand = new Vector();
        pop2Rand.add(Real.valueOf(4));

        //mock contribution selection that will return desired contribution
        final ContributionSelectionStrategy strategy = mock(ContributionSelectionStrategy.class);
        when(strategy.getContribution(Matchers.<PopulationBasedAlgorithm>anyObject())).thenReturn(pop1Result, pop2Result);

        final DomainRegistry problemDomain = new StringBasedDomainRegistry();
        problemDomain.setDomainString("R(0.0, 4.0)^2");

        //Mock problem
        final OptimisationProblem problem = mock(OptimisationProblem.class);
        when(problem.getClone()).thenReturn(problem);
        when(problem.getDomain()).thenReturn(problemDomain);

        final OptimisationSolution solution = new OptimisationSolution(pop1Rand, InferiorFitness.instance());

        final CooperativeCoevolutionProblemAdapter subProb = mock(CooperativeCoevolutionProblemAdapter.class);
        when(subProb.getProblemAllocation()).thenReturn(new SequencialDimensionAllocation(0, 1), new SequencialDimensionAllocation(1, 1));


        //Mock participating algorithms
        final PSO subPopulation = mock(PSO.class);
        when(subPopulation.getOptimisationProblem()).thenReturn(subProb);
        when(subPopulation.getClone()).thenReturn(subPopulation);
        when(subPopulation.getContributionSelectionStrategy()).thenReturn(strategy);
        when(subPopulation.getBestSolution()).thenReturn(solution);

        final CooperativeCoevolutionAlgorithm testAlgorithm = new CooperativeCoevolutionAlgorithm();

        testAlgorithm.addPopulationBasedAlgorithm(subPopulation);
        testAlgorithm.addPopulationBasedAlgorithm(subPopulation);

        final ProblemDistributionStrategy distribution = mock(ProblemDistributionStrategy.class);
        testAlgorithm.setProblemDistribution(distribution);

        //mock fintess calculator for context entity
        final FitnessCalculator<Entity> calculator = mock(FitnessCalculator.class);
        when(calculator.getFitness(Matchers.<Entity>anyObject())).thenReturn(InferiorFitness.instance(), new MinimisationFitness(2.0), new MinimisationFitness(1.0));
        when(calculator.getClone()).thenReturn(calculator);

        testAlgorithm.getContext().setFitnessCalculator(calculator);
        testAlgorithm.setContextUpdate(new SelectiveContextUpdateStrategy());
        testAlgorithm.setOptimisationProblem(problem);

        testAlgorithm.performInitialisation();

        testAlgorithm.performIteration();

        //ensure that the solutions from the sub populations have been copied into the context vector
        assertEquals(1.0, testAlgorithm.getContext().getCandidateSolution().get(0).doubleValue(), 0.0);
        assertEquals(2.0, testAlgorithm.getContext().getCandidateSolution().get(1).doubleValue(), 0.0);
        assertEquals(1.0, testAlgorithm.getContext().getFitness().getValue(), 1.0);

        verify(subProb, times(2)).updateContext(Matchers.<Vector>anyObject());
        verify(subPopulation, atLeastOnce()).performInitialisation();
        verify(subPopulation, atLeastOnce()).performIteration();
        verify(distribution, atLeastOnce()).performDistribution(Matchers.<List<PopulationBasedAlgorithm>>anyObject(), Matchers.<OptimisationProblem>anyObject(), Matchers.<Vector>anyObject());
        verify(calculator, atLeast(3)).getFitness(Matchers.<Entity>anyObject());
    }
}
