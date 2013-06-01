/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

/**
 * This is a control parameter that stores its value, allows for one to set it and
 * allows for one to update it
 */
public class AdaptableControlParameter implements ControlParameter{
    
    private double parameter;
    
    /**
     * Default constructor for AdaptableControlParameter
     */
    public AdaptableControlParameter() {
        parameter = 0;
    }
    
    public AdaptableControlParameter(double parameter) {
        this.parameter = parameter;
    }

    /**
     * Copy constructor for AdaptableControlParameter
     * @param copy The AdaptableControlParameter to be copied
     */
    public AdaptableControlParameter(AdaptableControlParameter copy) {
        parameter = copy.parameter;
    }
    
    /**
     * Clone method for AdaptableControlParameter
     * @return A new instance of this AdaptableControlParameter
     */
    @Override
    public AdaptableControlParameter getClone() {
        return new AdaptableControlParameter(this);
    }

    /**
     * This returns the parameter value of the AdaptableControlParameter
     * @return The parameter value
     */
    @Override
    public double getParameter() {
        return parameter;
    }

    /**
     * This is not supported for the AdaptableControlParameter class
     */
    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("StandardControlParameter has no bounds. Use a BoundedControlParameter instead.");
    }
    
    /**
     * Sets the value of the parameter to the one received
     * @param newParameter The new value for the parameter
     */
    public void setParameter(double newParameter) {
        parameter = newParameter;
    }

    /**
     * Updates the value of the parameter to be the one received
     * @param newParameter The new value for the parameter
     */
    public void update(double newParameter) {
        parameter = newParameter;
    }
    
}
