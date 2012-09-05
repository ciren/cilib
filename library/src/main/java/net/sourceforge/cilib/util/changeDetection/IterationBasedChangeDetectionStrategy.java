/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.changeDetection;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;

public class IterationBasedChangeDetectionStrategy extends ChangeDetectionStrategy{
    int iterationModulus;
    int nextIterationOfChange;
    
    /*
     * Default constructor for the IterationBasedChangeDetectionStrategy
     */
    public IterationBasedChangeDetectionStrategy() {
        iterationModulus = 1;
        nextIterationOfChange = 1;
    }
    
    /*
     * Copy constructor of the IterationBasedChangeDetectionStrategy
     * @param copy The IterationBasedChangeDetectionStrategy that must be copied
     */
    public IterationBasedChangeDetectionStrategy(IterationBasedChangeDetectionStrategy copy) {
        iterationModulus = copy.iterationModulus;
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
            nextIterationOfChange += iterationModulus;
            return true;
        }
        return false;
    }
    
    /*
     * Returns the iteration of change: the iteration at which a change in the dataset occurs.
     * @return iterationOfChange The Iteration when a change will occur
     */
    public int getIterationModulus() {
        return iterationModulus;
    }
    
    /*
     * Sets the iteration of change: the iteration at which a change in the dataset occurs.
     * @param changeIteration The new value for the iterationOfChange variable
     */
    public void setIterationModulus(int changeIteration) {
        iterationModulus = changeIteration;
        nextIterationOfChange = iterationModulus;
    }

}
