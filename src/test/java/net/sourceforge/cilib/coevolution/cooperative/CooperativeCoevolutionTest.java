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
package net.sourceforge.cilib.coevolution.cooperative;
import java.util.List;

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
import org.jmock.lib.legacy.ClassImposteriser;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import static org.junit.Assert.*;
public class CooperativeCoevolutionTest {
    private Mockery context = new Mockery()     {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};

    @Test
    public void CoevolutionTest(){

         //fake result vectors for the two subpopulation algorithms
         final Vector pop1Result = new Vector(1);
         pop1Result.add(new Real(1));
         final Vector pop2Result = new Vector(1);
         pop2Result.add(new Real(2));
         //fake random vectors for the two subpopulation algorithms
         final Vector pop1Rand = new Vector(1);
         pop1Rand.add(new Real(4));
         final Vector pop2Rand = new Vector(1);
         pop2Rand.add(new Real(4));

         //mock contribution selection that will return desired contribution
         final ContributionSelectionStrategy strategy = context.mock(ContributionSelectionStrategy.class);
         context.checking(new Expectations() {{
                 atLeast(2).of (strategy).getContribution(with(any(PopulationBasedAlgorithm.class)));
                will(onConsecutiveCalls(
                            returnValue(pop1Result),
                            returnValue(pop2Result)));
             }});

        final DomainRegistry problemDomain = new StringBasedDomainRegistry();
        problemDomain.setDomainString("R(0.0, 4.0)^2");

        //Mock problem
         final OptimisationProblem problem = context.mock(OptimisationProblem.class);
         context.checking(new Expectations() {{
                 allowing (problem).getClone(); will(returnValue(problem));
                 allowing (problem).getDomain(); will(returnValue(problemDomain));
            }});

         //mock solutions
         final OptimisationSolution solution = context.mock(OptimisationSolution.class);
         context.checking(new Expectations() {{
                 atLeast(2).of (solution).getPosition();
                will(onConsecutiveCalls(
                            returnValue(pop1Rand),
                            returnValue(pop2Rand)));
             }});

         final CooperativeCoevolutionProblemAdapter subProb = context.mock(CooperativeCoevolutionProblemAdapter.class);
         context.checking(new Expectations() {{
                 atLeast(2).of (subProb).updateContext(with(any(Vector.class)));
                 atLeast(2).of (subProb).getProblemAllocation();
                will(onConsecutiveCalls(
                        returnValue(new SequencialDimensionAllocation(0, 1)),
                        returnValue(new SequencialDimensionAllocation(1, 1))));
            }});

         //Mock participating algorithms
        final PSO subPopulation = context.mock(PSO.class);
         context.checking(new Expectations() {{
                 allowing (subPopulation).getOptimisationProblem(); will(returnValue(subProb));
                allowing (subPopulation).getClone(); will(returnValue(subPopulation));
                allowing (subPopulation).getContributionSelectionStrategy(); will(returnValue(strategy));
                allowing (subPopulation).getBestSolution(); will( returnValue(solution));
                atLeast(1).of (subPopulation).performInitialisation(); //check that this method is called
                atLeast(1).of (subPopulation).performIteration(); //check that this method is called
            }});

         final CooperativeCoevolutionAlgorithm testAlgorithm = new CooperativeCoevolutionAlgorithm();

         testAlgorithm.addPopulationBasedAlgorithm(subPopulation);
         testAlgorithm.addPopulationBasedAlgorithm(subPopulation);

         final ProblemDistributionStrategy distribution = context.mock(ProblemDistributionStrategy.class);
         context.checking(new Expectations() {{
             atLeast(1).of (distribution).performDistribution(with(any(List.class)), with(any(OptimisationProblem.class)), with(any(Vector.class)));
            }});

         testAlgorithm.setProblemDistribution(distribution);

         //mock fintess calculator for context entity
         final FitnessCalculator<Entity> calculator = context.mock(FitnessCalculator.class);
         context.checking(new Expectations() {{
                 atLeast(3).of (calculator).getFitness( with(any(ContextEntity.class)));
                will(onConsecutiveCalls(
                            returnValue(InferiorFitness.instance()), //first evaluation
                            returnValue(new MinimisationFitness(2.0)), //first population change
                            returnValue(new MinimisationFitness(1.0)))); //second pop change
                allowing (calculator).getClone(); will(returnValue(calculator));
            }});


         testAlgorithm.getContext().setFitnessCalculator(calculator);
         testAlgorithm.setContextUpdate(new SelectiveContextUpdateStrategy());
         testAlgorithm.setOptimisationProblem(problem);

         testAlgorithm.performInitialisation();

         testAlgorithm.performIteration();

         context.assertIsSatisfied(); //assert that alll the required methods have been invoked

         //ensure that the solutions from the sub populations have been copied into the context vector
         assertEquals(1.0, testAlgorithm.getContext().getCandidateSolution().get(0).getReal(), 0.0);
         assertEquals(2.0, testAlgorithm.getContext().getCandidateSolution().get(1).getReal(), 0.0);
         assertEquals(1.0, testAlgorithm.getContext().getFitness().getValue(), 1.0);
    }
}
