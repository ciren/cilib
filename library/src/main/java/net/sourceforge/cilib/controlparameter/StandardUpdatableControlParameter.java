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
public class StandardUpdatableControlParameter implements SettableControlParameter{
    private double parameter;
    
    /*
     * Default constructor for StandardUpdatableControlParameter
     */
    public StandardUpdatableControlParameter() {
        parameter = 0;
    }

    /*
     * Copy constructor for StandardUpdatableControlParameter
     * @param copy The StandardUpdatableControlParameter to be copied
     */
    public StandardUpdatableControlParameter(StandardUpdatableControlParameter copy) {
        parameter = copy.parameter;
    }
    
    /*
     * Clone method for StandardUpdatableControlParameter
     * @return A new instance of this StandardUpdatableControlParameter
     */
    @Override
    public SettableControlParameter getClone() {
        return new StandardUpdatableControlParameter(this);
    }

    /*
     * This returns the parameter value of the StandardUpdatableControlParameter
     * @return The parameter value
     */
    public double getParameter() {
        return parameter;
    }

    /*
     * This is not supported for the StandardUpdatableControlParameter class
     */
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("StandardControlParameter has no bounds. Use a BoundedControlParameter instead.");
    }
    
    /*
     * Sets the value of the parameter to the one received
     * @param newParameter The new value for the parameter
     */
    @Override
    public void setParameter(double newParameter) {
        parameter = newParameter;
    }

    /*
     * Updates the value of the parameter to be the one received
     * @param newParameter The new value for the parameter
     */
    @Override
    public void update(double newParameter) {
        parameter = newParameter;
    }
    
}
