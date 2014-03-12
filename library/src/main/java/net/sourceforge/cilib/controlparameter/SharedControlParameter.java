/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

public class SharedControlParameter implements ControlParameter {

    public SharedControlParameter() {
    }

    public SharedControlParameter(ControlParameter delegate) {
        this.delegate = delegate;
    }
    
    private ControlParameter delegate;

    @Override
    public double getParameter() {
        return delegate.getParameter();
    }

    @Override
    public double getParameter(double min, double max) {
        return delegate.getParameter(min, max);
    }

    @Override
    public ControlParameter getClone() {
        return this;
    }

    public void setDelegate(ControlParameter delegate) {
        this.delegate = delegate;
    }

}
