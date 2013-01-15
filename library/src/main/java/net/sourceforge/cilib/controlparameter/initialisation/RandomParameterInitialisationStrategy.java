/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.initialisation;

import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;

public class RandomParameterInitialisationStrategy implements ControlParameterInitialisationStrategy<SettableControlParameter> {
    private ProbabilityDistributionFunction random;
    
    public RandomParameterInitialisationStrategy() {
        random = new GaussianDistribution();
    }
    
    public RandomParameterInitialisationStrategy(RandomParameterInitialisationStrategy copy) {
        random = copy.random;
    }
    
    public ControlParameterInitialisationStrategy getClone() {
        return new RandomParameterInitialisationStrategy(this);
    }

    @Override
    public void initialise(SettableControlParameter parameter) {
        double value = random.getRandomNumber();
        parameter.setParameter(value);
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }
    
    
}
