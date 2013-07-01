/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.generic;

import net.cilib.pso.PSO;
import net.cilib.stoppingcondition.Maximum;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Assert;
import org.junit.Test;


/**
 *
 */
public class PercentageCompleteTest {

    @Test
    public void value() {
        PSO pso = new PSO();
        MeasuredStoppingCondition maximumIterations = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 100);
        pso.addStoppingCondition(maximumIterations);

        for (int i = 0; i < 10; i++)
            pso.performIteration();

        PercentageComplete percentageComplete = new PercentageComplete();
        Assert.assertEquals(0.1, percentageComplete.getValue(pso).doubleValue(), 0.001);
    }

}
