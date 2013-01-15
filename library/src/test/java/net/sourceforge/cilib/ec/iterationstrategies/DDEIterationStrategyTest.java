/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.util.selection.recipes.FeasibilitySelector;

public class DDEIterationStrategyTest {

    @Test
    public void getTargetVectorSelectionStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        FeasibilitySelector selector = new FeasibilitySelector();
        strategy.setTargetVectorSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getTargetVectorSelectionStrategy());
    }

    @Test
    public void setTargetVectorSelectionStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        FeasibilitySelector selector = new FeasibilitySelector();
        strategy.setTargetVectorSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getTargetVectorSelectionStrategy());
    }

    @Test
    public void getCrossoverStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        DifferentialEvolutionBinomialCrossover crossoverStr = new DifferentialEvolutionBinomialCrossover();
        strategy.setCrossoverStrategy(crossoverStr);

        Assert.assertEquals(crossoverStr, strategy.getCrossoverStrategy());
    }

    @Test
    public void setCrossoverStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        DifferentialEvolutionBinomialCrossover crossoverStr = new DifferentialEvolutionBinomialCrossover();
        strategy.setCrossoverStrategy(crossoverStr);

        Assert.assertEquals(crossoverStr, strategy.getCrossoverStrategy());
    }

    @Test
    public void getTrialVectorCreationStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        RandCreationStrategy creationStr = new RandCreationStrategy();
        strategy.setTrialVectorCreationStrategy(creationStr);

        Assert.assertEquals(creationStr, strategy.getTrialVectorCreationStrategy());
    }

    @Test
    public void setTrialVectorCreationStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        RandCreationStrategy creationStr = new RandCreationStrategy();
        strategy.setTrialVectorCreationStrategy(creationStr);

        Assert.assertEquals(creationStr, strategy.getTrialVectorCreationStrategy());
    }

    @Test
    public void getSelectorParameterTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        strategy.setSelectorParameter(5.0);

        Assert.assertTrue(strategy.getSelectorParameter() == 5.0);
    }

    @Test
    public void setSelectorParameterTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        strategy.setSelectorParameter(5.0);

        Assert.assertTrue(strategy.getSelectorParameter() == 5.0);
    }

    @Test
    public void getTotalOffspringTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        strategy.setTotalOffspring(2);

        Assert.assertTrue(strategy.getTotalOffspring() == 2);
    }

    @Test
    public void setTotalOffspringTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        strategy.setTotalOffspring(2);

        Assert.assertTrue(strategy.getTotalOffspring() == 2);
    }

    @Test
    public void getScalingFactorRandomTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        UniformDistribution random = new UniformDistribution();
        strategy.setScalingFactorRandom(random);

        Assert.assertEquals(random, strategy.getScalingFactorRandom());
    }

    @Test
    public void setScalingFactorRandomTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        UniformDistribution random = new UniformDistribution();
        strategy.setScalingFactorRandom(random);

        Assert.assertEquals(random, strategy.getScalingFactorRandom());
    }

    @Test
    public void getOffspringSelectionStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        FeasibilitySelector selector = new FeasibilitySelector();
        strategy.setOffspringSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getOffspringSelectionStrategy());
    }

    @Test
    public void setOffspringSelectionStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        FeasibilitySelector selector = new FeasibilitySelector();
        strategy.setOffspringSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getOffspringSelectionStrategy());
    }

    @Test
    public void getNextGenerationSelectionStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        FeasibilitySelector selector = new FeasibilitySelector();
        strategy.setNextGenerationSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getNextGenerationSelectionStrategy());
    }

    @Test
    public void setNextGenerationSelectionStrategyTest() {
        DDEIterationStrategy strategy = new DDEIterationStrategy();
        FeasibilitySelector selector = new FeasibilitySelector();
        strategy.setNextGenerationSelectionStrategy(selector);

        Assert.assertEquals(selector, strategy.getNextGenerationSelectionStrategy());
    }
}
