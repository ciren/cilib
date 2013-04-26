/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.stoppingcondition.MaintainedStoppingCondition;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoppingConditionDetectionStrategyTest {

    @Test
    public void testDetect() {

        StoppingConditionDetectionStrategy<PopulationBasedAlgorithm> detect = new StoppingConditionDetectionStrategy<PopulationBasedAlgorithm>();

        final PopulationBasedAlgorithm algorithm = mock(PopulationBasedAlgorithm.class);

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
