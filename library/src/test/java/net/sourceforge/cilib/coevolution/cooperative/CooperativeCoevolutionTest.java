/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contextupdate.SelectiveContextUpdateStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequentialDimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CooperativeCoevolutionTest {

    @Test
    public void CoevolutionTest() {

        //fake result vectors for the two subpopulation algorithms
        final Vector pop1Result = Vector.of(1.0);
        final Vector pop2Result = Vector.of(2.0);
        //fake random vectors for the two subpopulation algorithms
        final Vector pop1Rand = Vector.of(4.0);
        final Vector pop2Rand = Vector.of(4.0);

        //mock contribution selection that will return desired contribution
        final ContributionSelectionStrategy strategy = mock(ContributionSelectionStrategy.class);
        when(strategy.getContribution(any(PopulationBasedAlgorithm.class))).thenReturn(pop1Result, pop2Result);

        final DomainRegistry problemDomain = new StringBasedDomainRegistry();
        problemDomain.setDomainString("R(0.0:4.0)^2");

        //Mock problem
        final Problem problem = mock(Problem.class);
        when(problem.getClone()).thenReturn(problem);
        when(problem.getDomain()).thenReturn(problemDomain);

        final OptimisationSolution solution = new OptimisationSolution(pop1Rand, InferiorFitness.instance());

        final CooperativeCoevolutionProblemAdapter subProb = mock(CooperativeCoevolutionProblemAdapter.class);
        when(subProb.getProblemAllocation()).thenReturn(new SequentialDimensionAllocation(0, 1), new SequentialDimensionAllocation(1, 1));

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
        when(calculator.getFitness(any(ContextEntity.class))).thenReturn(InferiorFitness.instance(), new MinimisationFitness(2.0), new MinimisationFitness(1.0));
        when(calculator.getClone()).thenReturn(calculator);

        testAlgorithm.getContext().setFitnessCalculator(calculator);
        testAlgorithm.setContextUpdate(new SelectiveContextUpdateStrategy());
        testAlgorithm.setOptimisationProblem(problem);

        testAlgorithm.performInitialisation();
        
        testAlgorithm.performIteration();
        
        //ensure that the solutions from the sub populations have been copied into the context vector
        assertEquals(4.0, testAlgorithm.getContext().getCandidateSolution().get(0).doubleValue(), 0.0);
        assertEquals(2.0, testAlgorithm.getContext().getCandidateSolution().get(1).doubleValue(), 0.0);
        assertEquals(1.0, testAlgorithm.getContext().getFitness().getValue(), 1.0);

        verify(subProb, atLeast(2)).updateContext(any(Vector.class));
        verify(subPopulation, atLeast(1)).performInitialisation();
        verify(subPopulation, atLeast(1)).performIteration();
        verify(distribution, atLeast(1)).performDistribution(any(List.class), any(Problem.class), any(Vector.class));
    }
}
