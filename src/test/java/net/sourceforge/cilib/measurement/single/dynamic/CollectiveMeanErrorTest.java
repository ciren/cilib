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
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.DynamicOptimizationProblem;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.*;

/**
 * @author Julien Duhain
 */
public class CollectiveMeanErrorTest {

    @Test
    public void results() {
        final Algorithm algorithm = mock(Algorithm.class);
        final DynamicOptimizationProblem problem = mock(DynamicOptimizationProblem.class);
        final OptimisationSolution mockSolution1 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(1.0));

        when(algorithm.getBestSolution()).thenReturn(mockSolution1);
        when(algorithm.getOptimisationProblem()).thenReturn(problem);
        when(problem.getError(Vector.of(1.0))).thenReturn(5.0, 4.0, 0.0);
        when(algorithm.getIterations()).thenReturn(1, 2, 3);

        Measurement m = new CollectiveMeanError();
        Assert.assertEquals(5.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(4.5, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
        Assert.assertEquals(3.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

        verify(algorithm, times(3)).getBestSolution();
        verify(algorithm, times(3)).getOptimisationProblem();
        verify(problem, times(3)).getError(Matchers.<Type>anyObject());
        verify(algorithm, times(3)).getIterations();
    }
}
