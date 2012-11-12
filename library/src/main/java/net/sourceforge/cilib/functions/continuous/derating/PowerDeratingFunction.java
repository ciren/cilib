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
public class PowerDeratingFunction implements DeratingFunction {
    private ControlParameter radius;
    private ControlParameter alpha;
    
    public PowerDeratingFunction() {
        this.radius = ConstantControlParameter.of(0.25);
        this.alpha = ConstantControlParameter.of(2.0);
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
                1.0 : Math.pow(input.doubleValueOf(0) / radius.getParameter(), alpha.getParameter());
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
     * Set the value of <code>alpha</code>.
     * @param alpha The value to set.
     */
    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }

    /**
     * Get the value of alpha.
     * @return The value of alpha.
     */
    public double getAlpha() {
        return alpha.getParameter();
    }
}
