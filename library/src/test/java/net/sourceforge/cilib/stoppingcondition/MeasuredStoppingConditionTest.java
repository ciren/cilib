/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.generic.Iterations;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class MeasuredStoppingConditionTest {
    /**
     * Test of getPercentageCompleted method, of class MeasuredStoppingCondition.
     */
    @Test
    public void testGetPercentageCompleted() {
        Algorithm algorithm = Mockito.mock(Algorithm.class);
        Mockito.when(algorithm.getIterations()).thenReturn(0, 0, 1, 1, 0, 0, 1, 1, 2, 2);
        MeasuredStoppingCondition instance = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 2);
        
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
