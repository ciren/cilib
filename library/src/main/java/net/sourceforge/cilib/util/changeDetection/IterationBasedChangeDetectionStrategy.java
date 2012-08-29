/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.util.changeDetection;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;

/**
 *
 * @author Kris
 */
public class IterationBasedChangeDetectionStrategy extends ChangeDetectionStrategy{
    int iterationOfChange;
    int nextIterationOfChange;
    
    /*
     * Default constructor for the IterationBasedChangeDetectionStrategy
     */
    public IterationBasedChangeDetectionStrategy() {
        iterationOfChange = 1;
        nextIterationOfChange = 1;
    }
    
    /*
     * Copy constructor of the IterationBasedChangeDetectionStrategy
     * @param copy The IterationBasedChangeDetectionStrategy that must be copied
     */
    public IterationBasedChangeDetectionStrategy(IterationBasedChangeDetectionStrategy copy) {
        iterationOfChange = copy.iterationOfChange;
        nextIterationOfChange = copy.nextIterationOfChange;
    }
    
    /*
     * The clone method of the IterationBasedChangeDetectionStrategy
     * @return A new instance of this IterationBasedChangeDetectionStrategy
     */
    @Override
    public ChangeDetectionStrategy getClone() {
        return new IterationBasedChangeDetectionStrategy(this);
    }

    /*
     * Checks whether a change has occured by determining whether the current iteration is the one
     * where a change occurs
     * @return True if the current iteration is the iteration of a change, false if it is not
     */
    @Override
    public boolean detectChange() {
        if(nextIterationOfChange == AbstractAlgorithm.get().getIterations()) {
            nextIterationOfChange += iterationOfChange;
            return true;
        }
        return false;
    }
    
    /*
     * Returns the iteration of change: the iteration at which a change in the dataset occurs.
     * @return iterationOfChange The Iteration when a change will occur
     */
    public int getIterationOfChange() {
        return iterationOfChange;
    }
    
    /*
     * Sets the iteration of change: the iteration at which a change in the dataset occurs.
     * @param changeIteration The new value for the iterationOfChange variable
     */
    public void setIterationOfChange(int changeIteration) {
        iterationOfChange = changeIteration;
        nextIterationOfChange = iterationOfChange;
    }

}
