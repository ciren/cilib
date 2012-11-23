/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * This is the DE with Neighbouthood Search parameter adaptation strategy described 
 * in the beginign of Zhenyu, Tang and Yao's 2008 paper "Self-adaptive Differential
 * Evolution with Neighbourhood Search".
 */
public class NSDEParameterAdaptationStrategy implements ParameterAdaptationStrategy{
    private double scalingFactorProbability;
    private ProbabilityDistributionFunction random;
    private ProbabilityDistributionFunction probabilityCheckRandom;
    private ProbabilityDistributionFunction cauchyVariableRandom;
    
    /* 
     * Default constructor for the NSDEParameterAdaptationStrategy
     */
    public NSDEParameterAdaptationStrategy() {
        scalingFactorProbability = 0.5;
        random = new GaussianDistribution();
        ((GaussianDistribution) random).setMean(0.0);
        ((GaussianDistribution) random).setDeviation(0.5);
        
        probabilityCheckRandom = new UniformDistribution();
        ((UniformDistribution) probabilityCheckRandom).setLowerBound(ConstantControlParameter.of(0));
        ((UniformDistribution) probabilityCheckRandom).setUpperBound(ConstantControlParameter.of(1));
        
        cauchyVariableRandom = new CauchyDistribution();
        ((CauchyDistribution) cauchyVariableRandom).setScale(ConstantControlParameter.of(1));
    }
    
    /*
     * Copy constructor of the NSDEParameterAdaptationStrategy
     * @param copy The NSDEParameterAdaptationStrategy to be copied
     */
    public NSDEParameterAdaptationStrategy(NSDEParameterAdaptationStrategy copy) {
        scalingFactorProbability = copy.scalingFactorProbability;
        random = copy.random;
        probabilityCheckRandom = copy.probabilityCheckRandom;
        cauchyVariableRandom = copy.cauchyVariableRandom;
    }
    
    /*
     * Clone method of NSDEParameterAdaptationStrategy
     * @return A new instance of this NSDEParameterAdaptationStrategy
     */
    public ParameterAdaptationStrategy getClone() {
        return new NSDEParameterAdaptationStrategy(this);
    }

    /*
     * This changes the value of the parameter according to the
     * DE with Neighbourhood Search
     * @param parameter The parameter to be changed
     */
    public void change(SettableControlParameter parameter) {
        if(probabilityCheckRandom.getRandomNumber() < scalingFactorProbability) {
            parameter.update(random.getRandomNumber());
        } else {
            parameter.update(cauchyVariableRandom.getRandomNumber());
        }
    }

    /*
     * This function is not applicable to the DE with Neighbourhood Search
     */
    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        throw new UnsupportedOperationException("Not necessary for this adaptation strategy");
    }

    /*
     * This function is not applicable to the DE with Neighbourhood Search
     */
    public double recalculateAdaptiveVariables() {
        throw new UnsupportedOperationException("No adaptive variables to be recalculated");
    }

    /*
     * Returns the value of the scaling factor probability variable
     * @return The scaling factor probability
     */
    public double getScalingFactorProbability() {
        return scalingFactorProbability;
    }

    /*
     * Sets the value of the scaling factor probability to the one received as a parameter
     * @param scalingFactorProbability The new value for the scaling factor probability
     */
    public void setScalingFactorProbability(double scalingFactorProbability) {
        this.scalingFactorProbability = scalingFactorProbability;
    }
    
}
