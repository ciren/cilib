/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.single;

import net.cilib.algorithm.Algorithm;
import net.cilib.measurement.Measurement;
import net.cilib.problem.solution.InferiorFitness;
import net.cilib.problem.solution.OptimisationSolution;
import net.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class SolutionTest {

    @Test
    public void result() {
        final Algorithm algorithm = mock(Algorithm.class);
        final OptimisationSolution mockSolution = new OptimisationSolution(Vector.of(1.0), InferiorFitness.instance());

        when(algorithm.getBestSolution()).thenReturn(mockSolution);

        Measurement m = new Solution();
        Assert.assertEquals(m.getValue(algorithm).toString(), mockSolution.getPosition().toString());
    }
}
