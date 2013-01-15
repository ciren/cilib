/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import junit.framework.Assert;
import org.junit.Test;
import net.sourceforge.cilib.ec.iterationstrategies.SaDEIterationStrategy;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

public class SaDEIterationStrategyTest {

    @Test
    public void getTargetVectorSelectionStrategyTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        RandomSelector selector = new RandomSelector();
        strategy.setTargetVectorSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getTargetVectorSelectionStrategy());
    }

    @Test
    public void setTargetVectorSelectionStrategy() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        RandomSelector selector = new RandomSelector();
        strategy.setTargetVectorSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getTargetVectorSelectionStrategy());
    }

    @Test
    public void getFrequencyOfChangeTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setFrequencyOfChange(5);

        Assert.assertEquals(5, strategy.getFrequencyOfChange());
    }

    @Test
    public void setFrequencyOfChangeTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setFrequencyOfChange(5);

        Assert.assertEquals(5, strategy.getFrequencyOfChange());
    }

    @Test
    public void getFrequencyOfMeanRecalculationTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setFrequencyOfMeanRecalculation(3);

        Assert.assertEquals(3, strategy.getFrequencyOfMeanRecalculation());
    }

    @Test
    public void setFrequencyOfMeanRecalculationTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setFrequencyOfMeanRecalculation(3);

        Assert.assertEquals(3, strategy.getFrequencyOfMeanRecalculation());
    }

    @Test
    public void getNextChangeTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setNextChange(3);

        Assert.assertEquals(3, strategy.getNextChange());
    }

    @Test
    public void setNextChangeTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setNextChange(3);

        Assert.assertEquals(3, strategy.getNextChange());
    }

    @Test
    public void getNextMeanRecalculationTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setNextMeanRecalculation(6);

        Assert.assertEquals(6, strategy.getNextMeanRecalculation());
    }

    @Test
    public void setNextMeanRecalculationTest() {
        SaDEIterationStrategy strategy = new SaDEIterationStrategy();
        strategy.setNextMeanRecalculation(6);

        Assert.assertEquals(6, strategy.getNextMeanRecalculation());
    }

}
