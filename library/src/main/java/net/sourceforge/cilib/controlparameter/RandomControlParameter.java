/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * A control parameter that provides random parameter values, distributed
 * according to a specified probability distribution function. By default,
 * the distribution is uniform.
 */
public class RandomControlParameter implements ControlParameter {
    private ProbabilityDistributionFunction distribution;

    public RandomControlParameter() {
        this(new UniformDistribution());
    }

    public RandomControlParameter(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }

    @Override
    public RandomControlParameter getClone() {
        return new RandomControlParameter();
    }

    @Override
    public double getParameter() {
        return distribution.getRandomNumber();
    }
    
    @Override
    public double getParameter(double min, double max) {
        return distribution.getRandomNumber(min, max);
    }

    /**
     * This method overrides getParamter() to allow the use of distributions
     * that require more parameters than min and max.
     * @param parameters The parameters required by the ProbabilityDistributionFunction.
     * @return A random number.
     */
    public double getParameter(double... parameters) {
        return distribution.getRandomNumber(parameters);
    }

    public ProbabilityDistributionFunction getDistribution() {
        return distribution;
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }

    public void setParameter(double newParameter) {
        //not applicable
    }
}
