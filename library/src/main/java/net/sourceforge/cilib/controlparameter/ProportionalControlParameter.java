/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * that is defined to return a proportional value.
 */
public class ProportionalControlParameter implements ControlParameter {

    private static final long serialVersionUID = 8415953407107514281L;
    private double proportion;

    public ProportionalControlParameter() {
        this.proportion = 0.1;
    }

    public ProportionalControlParameter(double proportion) {
        this.proportion = proportion;
    }

    public ProportionalControlParameter(ProportionalControlParameter copy) {
        this.proportion = copy.proportion;
    }

    @Override
    public ProportionalControlParameter getClone() {
        return new ProportionalControlParameter(this);
    }

    @Override
    public double getParameter() {
        return this.proportion;
    }

    @Override
    public double getParameter(double min, double max) {
        return this.proportion * (max - min);
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }
    
    public void setParameter(double newParameter) {
        proportion = newParameter;
    }
}
