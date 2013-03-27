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

public class GammaDistribution implements ProbabilityDistributionFunction {

    private ControlParameter shape;
    private ControlParameter scale;

    public GammaDistribution() {
        shape = ConstantControlParameter.of(2.0);
        scale = ConstantControlParameter.of(2.0);
    }

    /**
     * Get a Gamma-distributed random number with shape {@code k} and scale
     * {@code theta}.
     *
     * @return a Gamma-distributed random number.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(shape.getParameter(), scale.getParameter());
    }

    /**
     * Get a Gamma-distributed random number. Two parameters are required.
     * The first specifies the shape, the second specifies the scale.
     * <p>
     * This method takes advantage of the following relationship between
     * the Gamma and Exponential distributions:
     * <p>
     * if X1...Xn ~ Exponential(lambda) are exponentially distributed
     * and Y = X1 + X2 + ... + Xn, then Y ~ Gamma(n, 1/lambda).
     *
     * @param shapeScale    the shape and scale of the Gamma distribution.
     *                      {@code shape} is assumed to be an integer.
     * @return a Gamma-distributed random number.
     */
    @Override
    public double getRandomNumber(double... shapeScale) {
        checkArgument(shapeScale.length == 2, "The Gamma distribution requires two parameters.");
        checkArgument(shapeScale[0] > 0, "The first provided parameter (shape parameter) must be an integer greater than zero.");
        checkArgument(shapeScale[1] > 0, "The second provided parameter (scale parameter) must be greater than zero.");

        ProbabilityDistributionFunction expPdf = new ExponentialDistribution();
        double sum = 0;

        for (int i = 0; i < shapeScale[0]; i++) {
            sum += expPdf.getRandomNumber(1 / shapeScale[1]);
        }

        return sum;
    }

    public void setShape(ControlParameter shape) {
        this.shape = shape;
    }

    public ControlParameter getShape() {
        return shape;
    }

    public void setScale(ControlParameter scale) {
        this.scale = scale;
    }

    public ControlParameter getScale() {
        return scale;
    }
}
