/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

/**
 * An interface for Control Parameters that require a set and update method
 */
public interface SettableControlParameter extends ControlParameter{
    public void setParameter(double newParameter);
    
    public void update(double newParameter);
    
    /**
     * {@inheritDoc}
     */
    @Override
    SettableControlParameter getClone();
}
