/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.derating;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * R^1
 */
public class ExponentialDeratingFunction implements DeratingFunction {
    private ControlParameter radius;
    private ControlParameter m;
    
    public ExponentialDeratingFunction() {
        this.radius = ConstantControlParameter.of(0.25);
        this.m = ConstantControlParameter.of(0.01);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        if (input.size() != 1) {
            throw new RuntimeException("Derating functions may only be used in one dimension.");
        }

        if (input.doubleValueOf(0) >= radius.getParameter()) {
            return 1.0;
        }

        return input.doubleValueOf(0) >= radius.getParameter() ? 
                1.0 : Math.exp(Math.log(m.getParameter()) * (radius.getParameter() - input.doubleValueOf(0)) / radius.getParameter());
    }
    
    /**
     * Set the value of the radius.
     * @param radius The value to set.
     */
    public void setRadius(ControlParameter radius) {
        this.radius = radius;
    }

    /**
     * Get the value of the radius.
     * @return The value of the radius.
     */
    @Override
    public double getRadius() {
        return radius.getParameter();
    }

    /**
     * Set the value of <code>m</code>.
     * @param m The value to set.
     */
    public void setM(ControlParameter m) {
        this.m = m;
    }

    /**
     * Get the value of m.
     * @return The value of m.
     */
    public double getM() {
        return m.getParameter();
    }
}
