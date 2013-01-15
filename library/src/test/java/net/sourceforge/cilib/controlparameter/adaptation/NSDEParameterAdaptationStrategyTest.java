/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import junit.framework.Assert;
import org.junit.Test;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;

public class NSDEParameterAdaptationStrategyTest {
    @Test
    public void changeTest() {
        NSDEParameterAdaptationStrategy strategy = new NSDEParameterAdaptationStrategy();
        strategy.setScalingFactorProbability(1.0);
        StandardUpdatableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(5.0);

        strategy.change(parameter);
        Assert.assertTrue(parameter.getParameter() != 5.0);
    }

    @Test
    public void getScalingFactorProbabilityTest() {
        NSDEParameterAdaptationStrategy strategy = new NSDEParameterAdaptationStrategy();
        strategy.setScalingFactorProbability(0.9);

        Assert.assertEquals(0.9, strategy.getScalingFactorProbability());
    }

    @Test
    public void setScalingFactorProbabilityTest() {
        NSDEParameterAdaptationStrategy strategy = new NSDEParameterAdaptationStrategy();
        strategy.setScalingFactorProbability(0.9);

        Assert.assertEquals(0.9, strategy.getScalingFactorProbability());
    }
}
