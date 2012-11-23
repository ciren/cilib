/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;

/**
 * A Control parameter that varies exponentially according to the completed percentage of the algorithm.
 */
public class ExponentiallyVaryingControlParameter implements ControlParameter {
    
    private double initialValue;
    private double finalValue;
    private double curve;
    
    public ExponentiallyVaryingControlParameter() {
        this.initialValue = 0.0;
        this.finalValue = 1.0;
        this.curve = 1.0;
    }
    
    public ExponentiallyVaryingControlParameter(ExponentiallyVaryingControlParameter copy) {
        this.initialValue = copy.initialValue;
        this.finalValue = copy.finalValue;
        this.curve = copy.curve;
    }

    @Override
    public ExponentiallyVaryingControlParameter getClone() {
        return new ExponentiallyVaryingControlParameter(this);
    }
    
    @Override
    public double getParameter() {
        return getParameter(initialValue, finalValue);
    }
    
    @Override
    public double getParameter(double initialVal, double finalVal) {
        return initialVal + (finalVal - initialVal) 
                * (Math.exp(AbstractAlgorithm.get().getPercentageComplete() * curve) - 1) 
                / (Math.exp(curve) - 1) ;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue = finalValue;
    }

    public double getFinalValue() {
        return finalValue;
    }

    public void setCurve(double curve) {
        this.curve = curve;
    }

    public double getCurve() {
        return curve;
    }
    
    public void setParameter(double newParameter) {
        finalValue = newParameter;
    }
}
