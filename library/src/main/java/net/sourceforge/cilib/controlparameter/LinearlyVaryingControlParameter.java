/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;

/**
 * A Control parameter that varies linearly according to the completed percentage of the algorithm.
 */
public class LinearlyVaryingControlParameter implements ControlParameter {
    
    private double initialValue;
    private double finalValue;
    
    public LinearlyVaryingControlParameter() {
        this(0.0, Double.MAX_VALUE);
    }

    public LinearlyVaryingControlParameter(double initialValue, double finalValue) {
        this.initialValue = initialValue;
        this.finalValue = finalValue;
    }
    
    public LinearlyVaryingControlParameter(LinearlyVaryingControlParameter copy) {
        this.initialValue = copy.initialValue;
        this.finalValue = copy.finalValue;
    }
    
    @Override
    public LinearlyVaryingControlParameter getClone() {
        return new LinearlyVaryingControlParameter(this);
    }

    @Override
    public double getParameter() {
        return getParameter(initialValue, finalValue);
    }
    
    @Override
    public double getParameter(double initialVal, double finalVal) {
        return initialVal + (finalVal - initialVal) * AbstractAlgorithm.get().getPercentageComplete();
    } 

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue = finalValue;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public double getFinalValue() {
        return finalValue;
    }
    
    public void setParameter(double newParameter) {
        finalValue = newParameter;
    }
}
