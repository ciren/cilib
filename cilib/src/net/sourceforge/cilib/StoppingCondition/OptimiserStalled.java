/*
 * OptimiserStalled.java
 *
 * Created on August 30, 2004, 10:19 PM
 */

package net.sourceforge.cilib.StoppingCondition;

import net.sourceforge.cilib.Algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.Problem.OptimisationSolution;
import net.sourceforge.cilib.Util.AbsoluteDistanceMeasure;
import net.sourceforge.cilib.Util.DistanceMeasure;

/** used to check if optimisation algorithm has stalled.
 *
 *  still a very crude implementation. if the distance from the current best is less then the specified minimum for more then the 
 *  specified maximum consecutive minimum change, then the algorithm is assumed to have stalled.
 *
 * @author  frans
 */
public class OptimiserStalled implements StoppingCondition{
    
    /** Creates a new instance of OptimiserStalled */
    public OptimiserStalled() {
        minChange = 0.05;
        maxConsecutiveMinChange = 5;
   
        minChangeCounter = 0;
        distMeasure = new AbsoluteDistanceMeasure();
    }
    
    public double getPercentageCompleted() {
        //check if this is the first iteration
        if (previousBest == null) {
            previousBest = algorithm.getBestSolution();
            
            return 0.0;
        }
        
        //get the distance between previous and current best
        double distance =  distMeasure.distance((double[])previousBest.getPosition(), (double[])algorithm.getBestSolution().getPosition());
        
        //compare to see change
        if (distance < minChange)
            minChangeCounter++;
        else
            minChangeCounter = 0;
        
        return minChangeCounter / maxConsecutiveMinChange;
    }
    
    public boolean isCompleted() {
        if (getPercentageCompleted() == 1.0)
            return true;
        
        previousBest = algorithm.getBestSolution();
        return false;
    }
    
    public void setAlgorithm(net.sourceforge.cilib.Algorithm.Algorithm algorithm) {
        this.algorithm = (OptimisationAlgorithm) algorithm;
    }

    /**sets the minimum percentage that the new best location must be from the previous
     */
    public void setMinimumChange(double distance) {
        minChange = distance;
    }

    /**sets the maximum consecutive evalutions that an algorithm can improve less then the minimum percentage.
     *   this value relates to evaluations and not iterations. 
     */
    public void setMaxConsecutiveMinChange(int count) {
        maxConsecutiveMinChange = count;
    }
    
    protected double minChange;
    protected int maxConsecutiveMinChange;
    protected int minChangeCounter;
    
    DistanceMeasure distMeasure;
    OptimisationAlgorithm algorithm;
    OptimisationSolution previousBest;
}
