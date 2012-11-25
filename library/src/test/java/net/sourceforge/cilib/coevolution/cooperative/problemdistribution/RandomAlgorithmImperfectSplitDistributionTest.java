/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.problemdistribution;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.Problem;
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

public class RandomAlgorithmImperfectSplitDistributionTest {
    @Test
    public void RandomAlgorithmImperfectSplitTest(){
        Rand.setSeed(0);
        final DomainRegistry problemDomain = new StringBasedDomainRegistry();
        problemDomain.setDomainString("R(0.0:4.0)^5");
        Bounds bounds = new Bounds(0.0, 4.0);
        Vector data = Vector.of(Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds),
            Real.valueOf(0.0, bounds));

        List<PopulationBasedAlgorithm> populations = Arrays.asList((PopulationBasedAlgorithm)new PSO(), (PopulationBasedAlgorithm)new PSO());

        final Problem problem = mock(Problem.class);

        when(problem.getDomain()).thenReturn(problemDomain);

        RandomAlgorithmImperfectSplitDistribution test = new RandomAlgorithmImperfectSplitDistribution();
        test.performDistribution(populations, problem, data);

        CooperativeCoevolutionProblemAdapter p1 = (CooperativeCoevolutionProblemAdapter)populations.get(0).getOptimisationProblem();
        CooperativeCoevolutionProblemAdapter p2 = (CooperativeCoevolutionProblemAdapter)populations.get(1).getOptimisationProblem();

        assertEquals(2, p1.getDomain().getDimension(), 0.0);
        assertEquals(3, p2.getDomain().getDimension(), 0.0);

        assertEquals(3, p1.getProblemAllocation().getProblemIndex(0), 0.0);
        assertEquals(4, p1.getProblemAllocation().getProblemIndex(1), 0.0);

        assertEquals(0, p2.getProblemAllocation().getProblemIndex(0), 0.0);
        assertEquals(1, p2.getProblemAllocation().getProblemIndex(1), 0.0);
        assertEquals(2, p2.getProblemAllocation().getProblemIndex(2), 0.0);
    }
}
