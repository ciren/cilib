/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Activation functions are functions that are typically used within Neurons. This class provides
 * an abstraction for all functions that can be used in this manner.
 */
public interface ActivationFunction extends Function<Real, Real>, Differentiable, Cloneable {

    /**
     * {@inheritDoc }
     */
    @Override
    ActivationFunction getClone();

    /**
     * Determine the gradient of the {@link ActivationFunction} at the given point.
     * @param number The <code>point</code> at which the gradient is to be determined.
     * @return The value of the gradient and the provided input.
     */
    double getGradient(double number);

    /**
     * Evaluates the point given a double (as opposed to Real). And also returns
     * a double. This increases scalability and performance in the NN code.
     * @param input the point to evaluate.
     * @return the evaluation result.
     */
    double apply(double input);

    /**
     *  Return the lowerbound for the active range of this NeuronFunction
     * @return the lowerbound
     */
    double getLowerActiveRange();

    /**
     * Return the upperbound for the active range of this NeuronFunction
     * @return the upperbound
     */
    double getUpperActiveRange();
}
