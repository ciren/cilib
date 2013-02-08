/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.hybrid.cec2013;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.FixedControlParameter;
import net.sourceforge.cilib.controlparameter.RandomControlParameter;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * This is a container class to store information about individual functions used
 * in hybrid composite functions of the CEC2013 benchmark functions. Shifting is done
 * through here rather than using separate decorator classes.
 * <p>
 * Parameters that must be set:
 * </p>
 * <ul>
 * <li>sigma</li>
 * <li>lambda: scaling factor</li>
 * <li>bias: vertical shifting</li>
 * <li>function: the optimization function that makes up the overall function</li>
 * </ul>
 * <p>
 * </p>
 * <p>
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 */
public class SingleFunction implements ContinuousFunction {
    private ContinuousFunction function;
    private double sigma;
    private double lambda;
    private ControlParameter horizontalShift;
    private double bias;

    /**
     * Default constructor.
     */
    public SingleFunction() {
        this.sigma = 1.0;
        this.lambda = 1.0;
        this.bias = 0.0;

        // set up the default horizontal shift used for all
        // cec2013 functions. This can be changed by using the setter.
        FixedControlParameter f = new FixedControlParameter();
        RandomControlParameter r = new RandomControlParameter();
        UniformDistribution u = new UniformDistribution();
        u.setLowerBound(ConstantControlParameter.of(-80.0));
        u.setUpperBound(ConstantControlParameter.of(80.0));
        r.setDistribution(u);
        f.setControlParameter(r);
        this.horizontalShift = f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        return lambda * function.apply(input) + bias;
    }

    /**
     * Get the weight contribution of the function with
     * respect to the input vector.
     * @param input The input vector to the function.
     * @return The weight contribution.
     */
    public double getWeight(Vector input) {
        double distance = 0.0;
        double shift = horizontalShift.getParameter();

        for (int i = 0; i < input.size(); i++) {
            distance += Math.pow(input.doubleValueOf(i) - shift, 2);
        }

        double expTerm = Math.exp(-distance / (2 * input.size() * sigma * sigma));

        return (1.0 / distance) * expTerm;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getSigma() {
        return sigma;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getLambda() {
        return lambda;
    }

    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    public ContinuousFunction getFunction() {
        return function;
    }

    public void setHorizontalShift(ControlParameter horizontalShift) {
        this.horizontalShift = horizontalShift;
    }

    public ControlParameter getHorizontalShift() {
        return horizontalShift;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
