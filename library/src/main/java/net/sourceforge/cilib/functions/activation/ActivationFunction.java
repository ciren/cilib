/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.Cloneable;
import fj.F;

/**
 * Activation functions are functions that are typically used within Neurons. This class provides
 * an abstraction for all functions that can be used in this manner.
 */
public abstract class ActivationFunction extends F<Real, Real> implements Differentiable, Cloneable {

    /**
     * {@inheritDoc }
     */
    @Override
    public abstract ActivationFunction getClone();

    /**
     * Determine the gradient of the {@link ActivationFunction} at the given point.
     * @param number The <code>point</code> at which the gradient is to be determined.
     * @return The value of the gradient and the provided input.
     */
    public abstract double getGradient(double number);

    /**
     * Evaluates the point given a double (as opposed to Real). And also returns
     * a double. This increases scalability and performance in the NN code.
     * @param input the point to evaluate.
     * @return the evaluation result.
     */
    public abstract double f(double input);

    /**
     *  Return the lowerbound for the active range of this NeuronFunction
     * @return the lowerbound
     */
    public abstract double getLowerActiveRange();

    /**
     * Return the upperbound for the active range of this NeuronFunction
     * @return the upperbound
     */
    public abstract double getUpperActiveRange();
}
