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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;

import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 */
public class CollectiveMeanOfMeasurementTest {

    @Test
    public void results() {
        final Algorithm algorithm = mock(Algorithm.class);
        final OptimisationSolution mockSolution1 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(3.0));
        final OptimisationSolution mockSolution2 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(1.0));
        final OptimisationSolution mockSolution3 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(2.3));
        final OptimisationSolution mockSolution4 = new OptimisationSolution(Vector.of(1.0), new MinimisationFitness(1.0));

        Measurement m = new CollectiveMeanOfMeasurement();
		
		when(algorithm.getIterations()).thenReturn(0);
        when(algorithm.getBestSolution()).thenReturn(mockSolution1);
        Assert.assertEquals(3.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

		when(algorithm.getIterations()).thenReturn(0);
        when(algorithm.getBestSolution()).thenReturn(mockSolution2);
        Assert.assertEquals(3.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(1);
        when(algorithm.getBestSolution()).thenReturn(mockSolution2);
        Assert.assertEquals(2.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(1);
        when(algorithm.getBestSolution()).thenReturn(mockSolution3);
        Assert.assertEquals(2.0, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);

		when(algorithm.getIterations()).thenReturn(2);
        when(algorithm.getBestSolution()).thenReturn(mockSolution3);
        Assert.assertEquals(2.1, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(2);
        when(algorithm.getBestSolution()).thenReturn(mockSolution4);
        Assert.assertEquals(2.1, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
		
		when(algorithm.getIterations()).thenReturn(3);
        when(algorithm.getBestSolution()).thenReturn(mockSolution4);
        Assert.assertEquals(1.825, ((Real) m.getValue(algorithm)).doubleValue(), 0.00001);
    }

}
