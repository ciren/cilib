/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ShuffleProblemRedistributionTest {

    @Test
    public void ShuffleProblemTest() {
        Rand.setSeed(0);
        final Problem problem1 = mock(Problem.class);
        final Problem problem2 = mock(Problem.class, "optimisationProblem2");
        final Problem problem3 = mock(Problem.class, "optimisationProblem3");

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
    }
}
