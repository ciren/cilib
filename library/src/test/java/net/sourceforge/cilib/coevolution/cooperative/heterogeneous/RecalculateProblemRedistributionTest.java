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
