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

public class UniformDistribution implements ProbabilityDistributionFunction {

    private ControlParameter lowerBound;
    private ControlParameter upperBound;

    /**
     * Default Constructor
     */
    public UniformDistribution() {
        lowerBound = ConstantControlParameter.of(0.0);
        upperBound = ConstantControlParameter.of(1.0);
    }

    /**
     * Contructor to initialize bounds to non-default values.
     * @param lowerBound The lower bound of the distribution.
     * @param upperBound The upper bound of the distribution.
     */
    public UniformDistribution(ControlParameter lowerBound, ControlParameter upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Get a uniform random number located within {@code 0 &lt;= x &lt; 1}.
     *
     * @return uniform random number ({@code 0 &lt;= x &lt; 1}).
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(lowerBound.getParameter(), upperBound.getParameter());
    }

    /**
     * Get the uniform random number. The number is located within {@code A &lt;= x &lt; B}
     * where {@code A == mean} and {@code B == deviation}.
     * <p>
     * Two parameters are required. The first specifies the lower bound,
     * the second specifies the upper bound.
     *
     * @param bounds the bounds for the number generation.
     * @return uniform random number ({@code lower &lt;= x &lt; upper}).
     */
    @Override
    public double getRandomNumber(double... bounds) {
        checkArgument(bounds.length == 2, "The Uniform distribution requires two parameters.");
        checkArgument(bounds[1] > bounds[0], "The lower bound (first parameter) must be less than upper bound (second parameter).");

        double r = Rand.nextDouble();
        return ((bounds[1] - bounds[0]) * r + bounds[0]);
    }

    public void setUpperBound(ControlParameter upperBound) {
        this.upperBound = upperBound;
    }

    public ControlParameter getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(ControlParameter lowerBound) {
        this.lowerBound = lowerBound;
    }

    public ControlParameter getLowerBound() {
        return lowerBound;
    }
}
