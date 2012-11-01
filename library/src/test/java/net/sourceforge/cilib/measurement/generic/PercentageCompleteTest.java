/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import static org.junit.Assert.*;
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
