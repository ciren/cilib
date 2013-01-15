/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import junit.framework.Assert;
import org.junit.Test;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.selection.recipes.FeasibilitySelector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

public class SaDDEIterationStrategyTest {

    @Test
    public void updateSelectorParameter() {
       SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
       strategy.setSelectorParameter(Real.valueOf(2.1, new Bounds(1,3)));
       strategy.setLastSelectorParameterValue(2.5);
       strategy.setFirstSelectorParameterValue(3.1);
       strategy.updateSelectorParameter(5);

       Assert.assertEquals(1.98, strategy.getSelectorParameter().doubleValue());
    }

    @Test
    public void getTargetVectorSelectionStrategy() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        RandomSelector selector = new RandomSelector();
        strategy.setTargetVectorSelectionStrategy(selector);
        Assert.assertEquals(selector, strategy.getTargetVectorSelectionStrategy());
    }

    @Test
    public void setTargetVectorSelectionStrategy() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        RandomSelector selector = new RandomSelector();
        strategy.setTargetVectorSelectionStrategy(selector);
        Assert.assertEquals(selector, strategy.getTargetVectorSelectionStrategy());
    }

    @Test
    public void getOffspringSelectionStrategy() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        FeasibilitySelector x = new FeasibilitySelector();
        strategy.setOffspringSelectionStrategy(x);
        Assert.assertEquals(x, strategy.getOffspringSelectionStrategy());
    }

    @Test
    public void setOffspringSelectionStrategy() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        FeasibilitySelector x = new FeasibilitySelector();
        strategy.setOffspringSelectionStrategy(x);
        Assert.assertEquals(x, strategy.getOffspringSelectionStrategy());
    }

    @Test
    public void getNextGenerationSelectionStrategy() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        FeasibilitySelector x = new FeasibilitySelector();
        strategy.setNextGenerationSelectionStrategy(x);
        Assert.assertEquals(x, strategy.getNextGenerationSelectionStrategy());
    }

    @Test
    public void setNextGenerationSelectionStrategy() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        FeasibilitySelector x = new FeasibilitySelector();
        strategy.setNextGenerationSelectionStrategy(x);
        Assert.assertEquals(x, strategy.getNextGenerationSelectionStrategy());
    }

    @Test
    public void getSelectorParameter() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        Real x = Real.valueOf(2.0, new Bounds(0,3));
        strategy.setSelectorParameter(x);
        Assert.assertEquals(x, strategy.getSelectorParameter());
    }

    @Test
    public void setSelectorParameter() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        Real x = Real.valueOf(2.0, new Bounds(0,3));
        strategy.setSelectorParameter(x);
        Assert.assertEquals(x, strategy.getSelectorParameter());
    }

    @Test
    public void getSelectorParameterRandom() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        UniformDistribution distribution = new UniformDistribution();
        strategy.setSelectorParameterRandom(distribution);
        Assert.assertEquals(distribution, strategy.getSelectorParameterRandom());
    }

    @Test
    public void setSelectorParameterRandom() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        UniformDistribution distribution = new UniformDistribution();
        strategy.setSelectorParameterRandom(distribution);
        Assert.assertEquals(distribution, strategy.getSelectorParameterRandom());
    }

    @Test
    public void getLastSelectorFactorRandom() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        UniformDistribution distribution = new UniformDistribution();
        strategy.setLastSelectorFactorRandom(distribution);
        Assert.assertEquals(distribution, strategy.getLastSelectorFactorRandom());
    }

    @Test
    public void setLastSelectorFactorRandom() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        UniformDistribution distribution = new UniformDistribution();
        strategy.setLastSelectorFactorRandom(distribution);
        Assert.assertEquals(distribution, strategy.getLastSelectorFactorRandom());
    }

    @Test
    public void getFirstSelectorParameterValue() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        strategy.setFirstSelectorParameterValue(2.0);
        Assert.assertEquals(2.0, strategy.getFirstSelectorParameterValue());
    }

    @Test
    public void setFirstSelectorParameterValue() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        strategy.setFirstSelectorParameterValue(2.0);
        Assert.assertEquals(2.0, strategy.getFirstSelectorParameterValue());
    }

    @Test
    public void getLastSelectorParameterValue() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        strategy.setLastSelectorParameterValue(2.0);
        Assert.assertEquals(2.0, strategy.getLastSelectorParameterValue());
    }

    @Test
    public void setLastSelectorParameterValue() {
        SaDDEIterationStrategy strategy = new SaDDEIterationStrategy();
        strategy.setLastSelectorParameterValue(2.0);
        Assert.assertEquals(2.0, strategy.getLastSelectorParameterValue());
    }

}
