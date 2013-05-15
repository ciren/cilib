/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * to represent the negative of another control parameter.
 */
public class NegativeOfControlParameter implements ControlParameter {

    private ControlParameter controlParameter;

    public NegativeOfControlParameter() {

    }

    public NegativeOfControlParameter(NegativeOfControlParameter copy) {
        this.controlParameter = copy.controlParameter.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NegativeOfControlParameter getClone() {
        return new NegativeOfControlParameter(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter() {
        return -controlParameter.getParameter();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("This operation remains to be implemented.");
    }

    /**
     * Sets the control parameter to negate.
     * @param controlParameter The control parameter.
     */
    public void setControlParameter(ControlParameter controlParameter) {
        this.controlParameter = controlParameter;
    }
}
