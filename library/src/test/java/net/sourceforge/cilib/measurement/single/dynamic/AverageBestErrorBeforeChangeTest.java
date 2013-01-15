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
 *
 */
public class AverageBestErrorBeforeChangeTest {

    @Test
    public void results() {
        final Algorithm algorithm = mock(Algorithm.class);
        final OptimisationSolution mockSolution1 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(0.0));
        final DynamicOptimisationProblem mockProblem = mock(DynamicOptimisationProblem.class);

        when(algorithm.getBestSolution()).thenReturn(mockSolution1);
        when(algorithm.getOptimisationProblem()).thenReturn(mockProblem);
        when(mockProblem.getError(Matchers.<Type>anyObject())).thenReturn(30.0, 10.0);
        when(algorithm.getIterations()).thenReturn(1, 2, 3, 4, 5, 6);

        AverageBestErrorBeforeChange m = new AverageBestErrorBeforeChange();
        m.setCycleSize(3);

        Assert.assertEquals(0.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(30.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(30.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(30.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(20.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(20.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

        verify(algorithm, times(2)).getBestSolution();
        verify(algorithm, times(2)).getOptimisationProblem();
        verify(algorithm, times(6)).getIterations();
        verify(mockProblem, times(2)).getError(Matchers.<Type>anyObject());
    }

}
