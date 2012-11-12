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
public class CauchyDistribution implements ProbabilityDistributionFunction {

    private RandomProvider provider;
    private ControlParameter location;
    private ControlParameter scale;

    /**
     * Default Constructor
     */
    public CauchyDistribution() {
        this.provider = new MersenneTwister();
        this.location = ConstantControlParameter.of(0.0);
        this.scale = ConstantControlParameter.of(1.0);
    }

    public CauchyDistribution(long seed) {
        this.provider = new MersenneTwister(seed);
        this.location = ConstantControlParameter.of(0.0);
        this.scale = ConstantControlParameter.of(1.0);
    }

    /**
     * Convenience method to obtain a Cauchy number. The distribution has a <code>mean</code>
     * of <code>0.0</code> and a <code>deviation</code> of <code>1.0</code>.
     * @return A cauchy number in the given distribution.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(location.getParameter(), scale.getParameter());
    }

    /**
     * Return a random number sampled from the Cauchy distribution.
     * Two parameters are required. The first specifies the location,
     * the second specifies the scale.
     * @param location The location of the mean of the distribution.
     * @param scale The allowed variation that can be observed.
     * @return A Cauchy random number with location <tt>location</tt> and
     *         scale parameter <tt>scale</tt>
     */
    @Override
    public double getRandomNumber(double... locationScale) {
        checkArgument(locationScale.length == 2, "The Cauchy distribution requires two parameters.");
        checkArgument(locationScale[1] > 0, "The scale must be greater than zero.");

        double x = provider.nextDouble(); // Uniform number between 0.0 and 1.0

        return locationScale[0] + locationScale[1] * Math.tan(Math.PI * (x - 0.5));
    }

    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }

    @Override
    public void setRandomProvider(RandomProvider provider) {
        this.provider = provider;
    }

    public void setScale(ControlParameter scale) {
        this.scale = scale;
    }

    public ControlParameter getScale() {
        return scale;
    }

    public void setLocation(ControlParameter location) {
        this.location = location;
    }

    public ControlParameter getLocation() {
        return location;
    }
}
