/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.creation;

import org.junit.Test;
import net.sourceforge.cilib.entity.operators.creation.SaDECreationStrategy;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import org.junit.Assert;
import org.junit.Test;

public class SaDECreationStrategyTest {

    @Test
    public void testUpdateProbabilities() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setProbability1(0.4);

        Assert.assertTrue(sade.getProbability1() == 0.4);

        sade.setTotalAcceptedWithStrategy1(3);
        sade.setTotalAcceptedWithStrategy2(4);
        sade.setTotalRejectedWithStrategy1(2);
        sade.setTotalRejectedWithStrategy2(1);

        sade.updateProbabilities();

        Assert.assertFalse(sade.getProbability1() == 0.4);
    }

    @Test
    public void testAccepted() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setProbability1(0.4);

        sade.accepted(true);

        Assert.assertTrue(sade.getTotalAcceptedWithStrategy1() + sade.getTotalAcceptedWithStrategy2() == 1);


        sade.accepted(false);
        sade.accepted(false);

        Assert.assertTrue(sade.getTotalRejectedWithStrategy1() + sade.getTotalRejectedWithStrategy2() == 2);
    }

    @Test
    public void testSetStrategy1() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setStrategy1(new RandToBestCreationStrategy());
        Assert.assertTrue(sade.getStrategy1() instanceof RandToBestCreationStrategy);
    }

    @Test
    public void testGetStrategy1() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setStrategy1(new RandToBestCreationStrategy());
        Assert.assertTrue(sade.getStrategy1() instanceof RandToBestCreationStrategy);
    }

    @Test
    public void testSetStrategy2() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setStrategy2(new RandToBestCreationStrategy());
        Assert.assertTrue(sade.getStrategy2() instanceof RandToBestCreationStrategy);
    }

    @Test
    public void testGetStrategy2() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setStrategy2(new RandToBestCreationStrategy());
        Assert.assertTrue(sade.getStrategy2() instanceof RandToBestCreationStrategy);
    }

    @Test
    public void testSetProbability1() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setProbability1(0.1);
        Assert.assertTrue(sade.getProbability1() == 0.1);
    }

    @Test
    public void testGetProbability1() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setProbability1(0.1);
        Assert.assertTrue(sade.getProbability1()  == 0.1);
    }

    @Test
    public void testSetRandom() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setRandom(new GaussianDistribution());
        Assert.assertTrue(sade.getRandom() instanceof GaussianDistribution);
    }

    @Test
    public void testGetRandom() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setRandom(new GaussianDistribution());
        Assert.assertTrue(sade.getRandom() instanceof GaussianDistribution);
    }

    @Test
    public void testSetLearningPeriod() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setLearningPeriod(30);
        Assert.assertTrue(sade.getLearningPeriod() == 30);
    }

    @Test
    public void testGetLearningPeriod() {
        SaDECreationStrategy sade = new SaDECreationStrategy();
        sade.setLearningPeriod(30);
        Assert.assertTrue(sade.getLearningPeriod() == 30);
    }
}
