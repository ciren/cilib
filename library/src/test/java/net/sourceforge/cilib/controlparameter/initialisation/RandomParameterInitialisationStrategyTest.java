/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.initialisation;

import junit.framework.Assert;
import org.junit.Test;
import net.sourceforge.cilib.math.random.GaussianDistribution;

public class RandomParameterInitialisationStrategyTest {

    @Test
    public void getRandomTest() {
        RandomParameterInitialisationStrategy strategy = new RandomParameterInitialisationStrategy();
        GaussianDistribution distribution = new GaussianDistribution();
        strategy.setRandom(distribution);

        Assert.assertEquals(distribution, strategy.getRandom());
    }

    @Test
    public void setRandomTest() {
        RandomParameterInitialisationStrategy strategy = new RandomParameterInitialisationStrategy();
        GaussianDistribution distribution = new GaussianDistribution();
        strategy.setRandom(distribution);

        Assert.assertEquals(distribution, strategy.getRandom());
    }
}
