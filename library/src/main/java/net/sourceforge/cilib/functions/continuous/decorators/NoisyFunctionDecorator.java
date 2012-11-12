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
 *
 *
 */
public class NoisyFunctionDecorator implements ContinuousFunction {

    private static final long serialVersionUID = -3918271655104447420L;
    private ContinuousFunction function;
    private ProbabilityDistributionFunction randomNumber;
    private ControlParameter variance;

    /**
     * Create an instance of the decorator and set the domain to "R" by default.
     */
    public NoisyFunctionDecorator() {
//        setDomain("R");
        randomNumber = new GaussianDistribution();
        this.variance = ConstantControlParameter.of(1.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        return function.apply(input) + randomNumber.getRandomNumber(0.0, this.variance.getParameter());
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

    public ControlParameter getVariance() {
        return variance;
    }

    public void setVariance(ControlParameter variance) {
        this.variance = variance;
    }
}
