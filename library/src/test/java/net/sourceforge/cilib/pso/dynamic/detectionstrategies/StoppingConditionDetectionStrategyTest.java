/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.stoppingcondition.MaintainedStoppingCondition;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoppingConditionDetectionStrategyTest {

    @Test
    public void testDetect() {

        StoppingConditionDetectionStrategy<SinglePopulationBasedAlgorithm> detect = new StoppingConditionDetectionStrategy<SinglePopulationBasedAlgorithm>();

        final SinglePopulationBasedAlgorithm algorithm = mock(SinglePopulationBasedAlgorithm.class);

        when(algorithm.getIterations()).thenReturn(0);
        assertFalse(detect.detect(algorithm));

        when(algorithm.getIterations()).thenReturn(500);
        assertFalse(detect.detect(algorithm));

        when(algorithm.getIterations()).thenReturn(999);
        assertFalse(detect.detect(algorithm));

        when(algorithm.getIterations()).thenReturn(1000);
        assertTrue(detect.detect(algorithm));

        when(algorithm.getIterations()).thenReturn(1001);
        assertTrue(detect.detect(algorithm));

        //test reset
        detect.setStoppingCondition( new MaintainedStoppingCondition(new MeasuredStoppingCondition(), 3));
        assertFalse(detect.detect(algorithm));
        assertFalse(detect.detect(algorithm));
        assertTrue(detect.detect(algorithm));
        assertFalse(detect.detect(algorithm));
        assertFalse(detect.detect(algorithm));
        assertTrue(detect.detect(algorithm));
        assertFalse(detect.detect(algorithm));
    }
}
