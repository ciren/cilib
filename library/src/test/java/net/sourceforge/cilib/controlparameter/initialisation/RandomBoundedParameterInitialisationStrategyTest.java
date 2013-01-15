/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.initialisation;

import junit.framework.Assert;
import org.junit.Test;
import net.sourceforge.cilib.controlparameter.initialisation.RandomBoundedParameterInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;

public class RandomBoundedParameterInitialisationStrategyTest {

    @Test
    public void initialiseTest() {
        StandardUpdatableControlParameter parameter = new StandardUpdatableControlParameter();
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        strategy.setLowerBound(1);
        strategy.setUpperBound(5);
        strategy.initialise(parameter);

        Assert.assertTrue(parameter.getParameter() > 1 && parameter.getParameter() < 5);
    }

    @Test
    public void getLowerBoundTest() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        strategy.setLowerBound(ConstantControlParameter.of(3));

        Assert.assertTrue(strategy.getLowerBound().getParameter() == 3);
    }

    @Test
    public void setLowerBoundTest() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        strategy.setLowerBound(3);

        Assert.assertTrue(strategy.getLowerBound().getParameter() == 3);
    }

    @Test
    public void setLowerBoundTest2() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        strategy.setLowerBound(ConstantControlParameter.of(3));

        Assert.assertTrue(strategy.getLowerBound().getParameter() == 3);
    }

    @Test
    public void getUpperBoundTest() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        strategy.setUpperBound(ConstantControlParameter.of(3));

        Assert.assertTrue(strategy.getUpperBound().getParameter() == 3);
    }

    @Test
    public void setUpperBoundTest() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        strategy.setUpperBound(ConstantControlParameter.of(3));

        Assert.assertTrue(strategy.getUpperBound().getParameter() == 3);
    }

    @Test
    public void setUpperBoundTest2() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        strategy.setUpperBound(3);

        Assert.assertTrue(strategy.getUpperBound().getParameter() == 3);
    }

    @Test
    public void getRandomTest() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        GaussianDistribution distribution = new GaussianDistribution();
        strategy.setRandom(distribution);

        Assert.assertEquals(distribution, strategy.getRandom());
    }

    @Test
    public void setRandomTest() {
        RandomBoundedParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        GaussianDistribution distribution = new GaussianDistribution();
        strategy.setRandom(distribution);

        Assert.assertEquals(distribution, strategy.getRandom());

    }
}
