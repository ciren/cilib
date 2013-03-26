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
import net.sourceforge.cilib.math.random.generator.Rand;

public class CauchyDistribution implements ProbabilityDistributionFunction {

    private ControlParameter location;
    private ControlParameter scale;

    /**
     * Default Constructor
     */
    public CauchyDistribution() {
        this.location = ConstantControlParameter.of(0.0);
        this.scale = ConstantControlParameter.of(1.0);
    }

    /**
     * Convenience method to obtain a Cauchy number. The distribution has a
     * {@code mean = 0.0} and {@code deviation = 1.0}.
     *
     * @return A Cauchy number in the given distribution.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(location.getParameter(), scale.getParameter());
    }

    /**
     * Return a random number sampled from the Cauchy distribution.
     * Two parameters are required. The first specifies the location,
     * the second specifies the scale.
     * @param locationScale the location (mean) and scale (deviation) of the
     *                      distribution.
     * @return A Cauchy number specified by {@code location} and {@code scale}.
     */
    @Override
    public double getRandomNumber(double... locationScale) {
        checkArgument(locationScale.length == 2, "The Cauchy distribution requires two parameters.");
        checkArgument(locationScale[1] > 0, "The scale must be greater than zero.");

        double x = Rand.nextDouble(); // Uniform number between 0.0 and 1.0

        return locationScale[0] + locationScale[1] * Math.tan(Math.PI * (x - 0.5));
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
