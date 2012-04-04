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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.generic.Iterations;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class MaintainedStoppingConditionTest {

    /**
     * Test of getPercentageCompleted method, of class MaintainedStoppingCondition.
     */
    @Test
    public void test() {
        Algorithm algorithm = Mockito.mock(Algorithm.class);
        Mockito.when(algorithm.getIterations()).thenReturn(0, 1, 0, 1, 2);
        MaintainedStoppingCondition instance = new MaintainedStoppingCondition(
                new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1), 2);
        
        assertFalse(instance.apply(algorithm));
        assertEquals(instance.getPercentageCompleted(algorithm), 0.0, 0.0);
        assertFalse(instance.apply(algorithm));
        assertEquals(instance.getPercentageCompleted(algorithm), 0.5, 0.0);        
        // if it gets reset percentage should not decrease
        assertFalse(instance.apply(algorithm));
        assertEquals(instance.getPercentageCompleted(algorithm), 0.5, 0.0);
        assertFalse(instance.apply(algorithm));
        assertEquals(instance.getPercentageCompleted(algorithm), 0.5, 0.0);
        assertTrue(instance.apply(algorithm));
        assertEquals(instance.getPercentageCompleted(algorithm), 1.0, 0.0);
    }
}
