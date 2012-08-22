/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.type.types.Bounds;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter} instance
 * that is defined to operate within a specific range of values. The range is defined as a domain string.
 * Any time the parameter exceeds the bounded range, it will be clamped and brought back to remain on
 * the edges of the range specified.
 *
 */
public class BoundedControlParameter implements ControlParameter {
    
    private Bounds bounds;
    private ControlParameter controlParameter;
    
    public BoundedControlParameter() {
        this.bounds = new Bounds(-Double.MAX_VALUE, Double.MAX_VALUE);
        this.controlParameter = new LinearlyVaryingControlParameter();
    }
    
    public BoundedControlParameter(BoundedControlParameter copy) {
        this.bounds = copy.bounds;
        this.controlParameter = copy.controlParameter.getClone();
    }
    
    @Override
    public BoundedControlParameter getClone() {
        return new BoundedControlParameter(this);
    }
    
    @Override
    public double getParameter() {
        return getParameter(bounds.getLowerBound(), bounds.getUpperBound());
    }
    
    @Override
    public double getParameter(double min, double max) {
        return Math.max(min, Math.min(max, controlParameter.getParameter()));
    }

    public Bounds getBounds() {
        return this.bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public ControlParameter getControlParameter() {
        return controlParameter;
    }

    public void setControlParameter(ControlParameter controlParameter) {
        this.controlParameter = controlParameter;
    }
    
}
