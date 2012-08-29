/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.util.changeDetection;

/**
 *
 * @author Kris
 */
public abstract class ChangeDetectionStrategy {
    /*
     * Clone method of the ChangeDetectionStrategy
     * @return A new instance of the ChangeDetectionStrategy
     */
    public abstract ChangeDetectionStrategy getClone();
    
    /*
     * Checks if a change has occured
     * @return True if a change has taken place, false if it has not
     */
    public abstract boolean detectChange();
}
