/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Adds noise to result of an evaluated function.
 * <p>
 * The noise is added as follows:
 * {@code functionResult * (offset + scale * |Gaussian(0,1)|)}
 * <p>
 * Reference:
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and
 * Tiwari, S. (2005). Problem Definitions and Evaluation Criteria for the CEC
 * 2005 Special Session on Real-Parameter Optimization. Natural Computing, 1-50.
 * Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 */
public class NoisyFunctionDecorator implements ContinuousFunction {

    private ContinuousFunction function;
    private ProbabilityDistributionFunction randomNumber;
    private ControlParameter scale;
    private ControlParameter offset;

    /**
     * Default constructor.
     */
    public NoisyFunctionDecorator() {
        this.randomNumber = new GaussianDistribution();
        this.scale = ConstantControlParameter.of(0.4);
        this.offset = ConstantControlParameter.of(1.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        return function.apply(input) * (offset.getParameter() + scale.getParameter() * Math.abs(randomNumber.getRandomNumber()));
    }

    /**
     * Get the function that is decorated.
     * @return Returns the noisyFunction.
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Set the decorated function.
     * @param function The function to decorate.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    /**
     * Gets the scale of the noise.
     * @return the scale of the noise.
     */
    public ControlParameter getScale() {
        return scale;
    }

    /**
     * Sets the scale of the noise.
     * @param noiseScale
     */
    public void setScale(ControlParameter noiseScale) {
        this.scale = noiseScale;
    }

    /**
     * Sets the offset of the noise.
     * @param offset
     */
    public void setOffset(ControlParameter offset) {
        this.offset = offset;
    }

    /**
     * Gets the offset of the noise.
     * @return the offset of the noise.
     */
    public ControlParameter getOffset() {
        return offset;
    }
}
