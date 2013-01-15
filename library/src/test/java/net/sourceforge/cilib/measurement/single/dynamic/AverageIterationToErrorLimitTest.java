/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.dynamic;


import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.DynamicOptimisationProblem;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.*;

/**
 */
public class AverageIterationToErrorLimitTest {

    @Test
    public void results() {
        final OptimisationSolution mockSolution1 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(0.0));
        final Algorithm algorithm = mock(Algorithm.class);
        final DynamicOptimisationProblem mockProblem = mock(DynamicOptimisationProblem.class);

        when(algorithm.getBestSolution()).thenReturn(mockSolution1);
        when(algorithm.getOptimisationProblem()).thenReturn(mockProblem);
        when(algorithm.isFinished()).thenReturn(false);
        when(algorithm.getIterations()).thenReturn(1, 2, 3, 4, 5, 6);
        when(mockProblem.getError(Matchers.<Type>anyObject())).thenReturn(7.0, 5.0, 35.0, 25.0, 5.0, 30.0);

        AverageIterationsToErrorLimit m = new AverageIterationsToErrorLimit();
        m.setCycleSize(3);
        m.setLimit(10.0);

        Assert.assertEquals(1.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(1.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(1.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(1.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(1.5, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(1.5, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

        verify(algorithm, times(6)).getBestSolution();
        verify(algorithm, times(6)).getOptimisationProblem();
        verify(algorithm, times(6)).getIterations();
        verify(algorithm, times(2)).isFinished();
        verify(mockProblem, times(6)).getError(Matchers.<Type>anyObject());
    }
}
