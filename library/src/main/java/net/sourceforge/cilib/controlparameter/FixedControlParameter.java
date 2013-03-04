/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

/**
 * A ControlParameter that maintains the first value it calculates.
 */
public class FixedControlParameter implements ControlParameter {
    
    private ControlParameter controlParameter;
    private double parameter = Double.NaN;
    
    public FixedControlParameter() {
        this.controlParameter = new RandomControlParameter();
        this.parameter = Double.NaN;
    }
    
    public FixedControlParameter(FixedControlParameter copy) {
        this.controlParameter = copy.controlParameter.getClone();
        this.parameter = Double.NaN;
    }
    
    @Override
    public FixedControlParameter getClone() {
        return new FixedControlParameter(this);
    }

    @Override
    public double getParameter() {
        if (Double.isNaN(parameter)) {
            parameter = controlParameter.getParameter();
        }
        
        return parameter;
    }

    @Override
    public double getParameter(double min, double max) {
        if (Double.isNaN(parameter)) {
            parameter = controlParameter.getParameter(min, max);
        }
        
        return parameter;
    }

    public void setControlParameter(ControlParameter controlParameter) {
        this.controlParameter = controlParameter;
    }

    public ControlParameter getControlParameter() {
        return controlParameter;
    }

}
