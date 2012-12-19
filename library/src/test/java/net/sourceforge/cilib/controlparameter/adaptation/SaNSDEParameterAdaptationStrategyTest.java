/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.ec.SaDEIndividual;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import org.junit.Test;

public class SaNSDEParameterAdaptationStrategyTest {
    @Test
    public void changeTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        StandardUpdatableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(5.0);
        double scalingFactorRecorded = strategy.getScalingFactorProbability();
        strategy.change(parameter);
        Assert.assertTrue(parameter.getParameter() != 5.0);
        
    }
    
    @Test
    public void acceptedTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        StandardUpdatableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(5.0);
        strategy.accepted(parameter, new SaDEIndividual(), true);
        
        Assert.assertTrue(strategy.getTotalAcceptedWithCauchy() == 5.0 || 
                strategy.getTotalAcceptedWithProbability() == 5.0);
    }
    
    @Test
    public void recalculateAdaptiveVariablesTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalRejectedWithCauchy(2);
        strategy.setTotalAcceptedWithCauchy(5);
        strategy.setTotalRejectedWithProbability(3);
        strategy.setTotalAcceptedWithProbability(7);
        strategy.recalculateAdaptiveVariables();
        
        Assert.assertEquals(Math.round(0.49494949 * 5) / 5, Math.round(strategy.getScalingFactorProbability() * 5) / 5);
    }
       
    @Test
    public void getScalingFactorProbabilityTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalRejectedWithCauchy(2);
        strategy.setTotalAcceptedWithCauchy(5);
        strategy.setTotalRejectedWithProbability(3);
        strategy.setTotalAcceptedWithProbability(7);
        strategy.recalculateAdaptiveVariables();
        
        Assert.assertEquals(Math.round(0.49494949 * 5) / 5, Math.round(strategy.getScalingFactorProbability() * 5) / 5);
    }
      
    @Test
    public void getRandomTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        GaussianDistribution random = new GaussianDistribution();
        strategy.setRandom(random);
        
        Assert.assertEquals(random, strategy.getRandom());
    }
      
    @Test
    public void setRandomTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        GaussianDistribution random = new GaussianDistribution();
        strategy.setRandom(random);
        
        Assert.assertEquals(random, strategy.getRandom());
    }
      
    @Test
    public void getCauchyVariableRandomTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        CauchyDistribution random = new CauchyDistribution();
        strategy.setCauchyVariableRandom(random);
        
        Assert.assertEquals(random, strategy.getCauchyVariableRandom());
    }
      
    @Test
    public void setCauchyVariableRandomTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        CauchyDistribution random = new CauchyDistribution();
        strategy.setCauchyVariableRandom(random);
        
        Assert.assertEquals(random, strategy.getCauchyVariableRandom());
    }
      
    @Test
    public void getTotalAcceptedWithProbabilityTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalAcceptedWithProbability(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalAcceptedWithProbability());
    }
      
    @Test
    public void setTotalAcceptedWithProbabilityTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalAcceptedWithProbability(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalAcceptedWithProbability());
    }
      
    @Test
    public void getTotalRejectedWithProbabilityTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalRejectedWithProbability(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalRejectedWithProbability());
    }
      
    @Test
    public void setTotalRejectedWithProbabilityTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalRejectedWithProbability(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalRejectedWithProbability());
    }
      
    @Test
    public void getTotalRejectedWithCauchyTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalRejectedWithCauchy(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalRejectedWithCauchy());
    }
      
    @Test
    public void setTotalRejectedWithCauchyTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalRejectedWithCauchy(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalRejectedWithCauchy());
    }
      
    @Test
    public void getTotalAcceptedWithCauchyTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalAcceptedWithCauchy(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalAcceptedWithCauchy());
    }
      
    @Test
    public void setTotalAcceptedWithCauchyTest() {
        SaNSDEParameterAdaptationStrategy strategy = new SaNSDEParameterAdaptationStrategy();
        strategy.setTotalAcceptedWithCauchy(3.1);
        
        Assert.assertEquals(3.1, strategy.getTotalAcceptedWithCauchy());
    }
}
