/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random;

import static com.google.common.base.Preconditions.checkArgument;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 */
public class ExponentialDistribution implements ProbabilityDistributionFunction {

    private RandomProvider provider;
    private ControlParameter rate;

    public ExponentialDistribution() {
        provider = new MersenneTwister();
        rate = ConstantControlParameter.of(1.0);
    }

    public ExponentialDistribution(long seed) {
        provider = new MersenneTwister(seed);
        rate = ConstantControlParameter.of(1.0);
    }

    /**
     * Get an Exponentially-distributed random number with default rate 1.0.
     * @return a Laplace-distributed random number with rate 1.0.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(rate.getParameter());
    }

    /**
     * Get an Exponentially-distributed random number. The rate of the distribution
     * is given by <code>rate</code>.
     *
     * @param rate The rate of the exponential distribution.
     * @return a Laplace-distributed random number.
     */
    @Override
    public double getRandomNumber(double... rate) {
        checkArgument(rate.length == 1, "The Exponential distribution requires a single parameter that specifies the rate.");
        checkArgument(rate[0] > 0, "The rate for the Exponential distribution parameter must be greater than zero.");

        double r = provider.nextDouble(); //uniform number in the range (0.0, 1.0]:
        return -Math.log(1 - r) / rate[0];
    }

    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }

    @Override
    public void setRandomProvider(RandomProvider provider) {
        this.provider = provider;
    }

    public ControlParameter getRate() {
        return rate;
    }

    public void setRate(ControlParameter rate) {
        this.rate = rate;
    }


}
