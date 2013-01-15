/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.initialisation;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * This is the initialisation strategy that initialises a parameter randomly 
 * within two bounds.
 */
public class RandomBoundedParameterInitialisationStrategy implements ControlParameterInitialisationStrategy<SettableControlParameter> {
    private SettableControlParameter lowerBound;
    private SettableControlParameter upperBound;
    private ProbabilityDistributionFunction random;
    
    /*
     * Default constructor for RandomBoundedParameterInitialisationStrategy
     */
     public RandomBoundedParameterInitialisationStrategy() {
        this.lowerBound = ConstantControlParameter.of(0.1);
        this.upperBound = ConstantControlParameter.of(0.1);
        this.random = new UniformDistribution();
    }

     /*
      * Copy constructor for RandomBoundedParameterInitialisationStrategy
      */
    public RandomBoundedParameterInitialisationStrategy(RandomBoundedParameterInitialisationStrategy copy) {
        this.lowerBound = copy.lowerBound.getClone();
        this.upperBound = copy.upperBound.getClone();
        this.random = copy.random;
    }
    
    /*
     * Clone method for RandomBoundedParameterInitialisationStrategy
     * @return The new instance of this RandomBoundedParameterInitialisationStrategy
     */
    public RandomBoundedParameterInitialisationStrategy getClone() {
        return new RandomBoundedParameterInitialisationStrategy(this);
    }

    /*
     * Initialises the parameter received within 2 bounds.
     * @param parameter The parameter to be initialised
     */
    @Override
    public void initialise(SettableControlParameter parameter) {
        double value = random.getRandomNumber(lowerBound.getParameter(), upperBound.getParameter());
        parameter.setParameter(value);
    }
    
    /*
     * Returns the lower bound of the RandomBoundedParameterInitialisationStrategy
     * @return The lower bound
     */
    public SettableControlParameter getLowerBound() {
        return lowerBound;
    }

    /*
     * Sets the lower bound of the RandomBoundedParameterInitialisationStrategy to the
     * parameter received
     * @param lowerBound The lower bound
     */
    public void setLowerBound(SettableControlParameter lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    /*
     * Sets the value of the bound of the RandomBoundedParameterInitialisationStrategy 
     * t the value received
     * @param lowerBound The lower bound
     */
     public void setLowerBound(double lowerBound) {
        this.lowerBound.setParameter(lowerBound);
    }

     /*
     * Returns the upper bound of the RandomBoundedParameterInitialisationStrategy
     * @return The upper bound
     */
    public SettableControlParameter getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(SettableControlParameter upperBound) {
        this.upperBound = upperBound;
    }
    
    public void setUpperBound(double upperBound) {
        this.upperBound.setParameter(upperBound);
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }
    
}
