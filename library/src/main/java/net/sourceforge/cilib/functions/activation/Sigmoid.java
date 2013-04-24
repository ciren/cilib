/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.activation;

import com.google.common.base.Objects;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The generalized sigmoid function. The function is the general case of the sigmoid function
 * with the ability to specify the lambda of the function as well as an offset that should
 * be taken into consideration.
 */
public class Sigmoid implements ActivationFunction {

    private ControlParameter lambda; // steepness
    private ControlParameter gamma;  // range
    private ControlParameter offset;

    /**
     * Create a new instance of {@code Sigmoid}. The default instance has the {@code lambda}
     * {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter} set
     * to a value of {@code 1.0}, with the {@code offset} defined as {@code 0.0}.
     */
    public Sigmoid() {
        this(1.0, 1.0);
    }

    public Sigmoid(double lambda, double gamma) {
        this.lambda = ConstantControlParameter.of(lambda);
        this.gamma = ConstantControlParameter.of(gamma);
        this.offset = ConstantControlParameter.of(0.0);
    }

    public Sigmoid(Sigmoid sigmoid) {
        this.lambda = sigmoid.getLambda().getClone();
        this.gamma = sigmoid.getGamma().getClone();
        this.offset = sigmoid.getOffset().getClone();
    }

    @Override
    public Sigmoid getClone() {
        return new Sigmoid(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real apply(Real input) {
        return Real.valueOf(apply(input.doubleValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double apply(double input) {
        return gamma.getParameter() / (1.0 + Math.exp(-1.0 * lambda.getParameter() * (input - offset.getParameter())));
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Sigmoid) && equals((Sigmoid) o);
    }

    private boolean equals(Sigmoid other) {
        return Objects.equal(getLambda(), other.getLambda()) &&
                Objects.equal(getGamma(), other.getGamma()) &&
                Objects.equal(getOffset(), other.getOffset());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLambda(), getGamma(), getOffset());
    }

    @Override
    public Vector getGradient(Vector x) {
        return Vector.of(this.getGradient(x.doubleValueOf(0)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        return number * (1 - number);
    }

    /**
     * Get the {@literal lambda} associated with the {@linkplain Sigmoid}.
     *
     * @return The {@linkplain ControlParameter} representing the {@literal lambda}.
     */
    public ControlParameter getLambda() {
        return lambda;
    }

    /**
     * Set the {@linkplain ControlParameter} to represent the {@literal lambda} of the function.
     *
     * @param lambda The value to set.
     */
    public void setLambda(ControlParameter lambda) {
        if (lambda.getParameter() < 0) {
            throw new UnsupportedOperationException("Cannot set lambda to a negative value.");
        }
        this.lambda = lambda;
    }

    /**
     * Get the {@literal gamma} associated with the {@linkplain Sigmoid}.
     *
     * @return The {@linkplain ControlParameter} representing the {@literal gamma}.
     */
    public ControlParameter getGamma() {
        return gamma;
    }

    /**
     * Set the {@linkplain ControlParameter} to represent the {@literal gamma} of the function.
     *
     * @param gamma The value to set.
     */
    public void setGamma(ControlParameter gamma) {
        this.gamma = gamma;
    }

    /**
     * Get the {@literal offset} associated with the function.
     *
     * @return The {@linkplain ControlParameter} representing the {@literal offset}.
     */
    public ControlParameter getOffset() {
        return offset;
    }

    /**
     * Set the {@linkplain ControlParameter} to represent the {@literal offset} of the function.
     *
     * @param offset The value to set.
     */
    public void setOffset(ControlParameter offset) {
        this.offset = offset;
    }

    /**
     * {@inheritDoc}
     * The active range for sigmoid is -Sqrt(3) - Sqrt(3), and Sqrt(3) = 1.732050808
     */
    @Override
    public double getLowerActiveRange() {
        return -1.732050808;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return 1.732050808;
    }
}
