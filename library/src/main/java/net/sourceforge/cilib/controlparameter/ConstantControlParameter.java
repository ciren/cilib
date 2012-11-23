/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import com.google.common.base.Objects;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * to represent a constant value. The specified value will be maintained until it is altered.
 */
public class ConstantControlParameter implements SettableControlParameter {
    private static final long serialVersionUID = 8847038781478109426L;
    protected double parameter;
    
    public static SettableControlParameter of(double value) {
        return new ConstantControlParameter(value);
    }

    /**
     * Create a new instance of {@code ConstantControlParameter}.
     */
    public ConstantControlParameter() {

    }

    /**
     * Create a new instance of {@linkplain net.sourceforge.cilib.controlparameter.ConstantControlParameter}
     * with the provided value as the value for the {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter}.
     * @param value The value to set.
     */
    protected ConstantControlParameter(double value) {
        this.parameter = value;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ConstantControlParameter(ConstantControlParameter copy) {
        this.parameter = copy.parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConstantControlParameter getClone() {
        return new ConstantControlParameter(this);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ConstantControlParameter) && equals((ConstantControlParameter)o);
    }

    private boolean equals(ConstantControlParameter other) {
        return Objects.equal(parameter, other.getParameter());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter() {
        return parameter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("ConstantControlParameter has no bounds. Use a BoundedControlParameter instead.");
    }

    /**
     * Sets the constant parameter.
     * 
     * @param value The new constant parameter.
     */
    @Override
    public void setParameter(double value) {
        this.parameter = value;
    }

    /*
     * This is method does nothing in this case as the ConstantControlParameter
     * cannot be updated, only set.
     */
    @Override
    public void update(double newParameter) {
       //nothing
    }
    
    
}
