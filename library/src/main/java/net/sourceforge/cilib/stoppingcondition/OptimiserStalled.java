/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.ManhattanDistanceMeasure;

/**
 * used to check if optimisation algorithm has stalled. still a very crude implementation. if the
 * distance from the current best is less then the specified minimum for more then the specified
 * maximum consecutive minimum change, then the algorithm is assumed to have stalled.
 */
public class OptimiserStalled implements StoppingCondition<Algorithm> {
    private static final long serialVersionUID = 4017249915571841835L;

    protected double minChange;
    protected int maxConsecutiveMinChange;
    protected int minChangeCounter;

    private final DistanceMeasure distMeasure;
    private OptimisationSolution previousBest;

    /**
     * Creates a new instance of OptimiserStalled.
     */
    public OptimiserStalled() {
        minChange = 0.05;
        maxConsecutiveMinChange = 5;

        minChangeCounter = 0;
        distMeasure = new ManhattanDistanceMeasure();
    }

    public OptimiserStalled(OptimiserStalled rhs) {
        minChange = rhs.minChange;
        maxConsecutiveMinChange = rhs.maxConsecutiveMinChange;

        minChangeCounter = rhs.minChangeCounter;
        distMeasure = rhs.distMeasure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimiserStalled getClone() {
        return new OptimiserStalled(this);
    }

    /**
     * sets the minimum percentage that the new best location must be from the previous.
     * @param distance The value to set.
     */
    public void setMinimumChange(double distance) {
        minChange = distance;
    }

    /**
     * sets the maximum consecutive evaluations that an algorithm can improve less then the minimum.
     * percentage. this value relates to evaluations and not iterations.
     * @param count The value to set.
     */
    public void setMaxConsecutiveMinChange(int count) {
        maxConsecutiveMinChange = count;
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        // check if this is the first iteration
        if (previousBest == null) {
            previousBest = algorithm.getBestSolution();

            return 0.0;
        }

        // get the distance between previous and current best
        double distance = distMeasure.distance((Vector) previousBest.getPosition(), (Vector) algorithm.getBestSolution().getPosition());

        // compare to see change
        if (distance < minChange)
            minChangeCounter++;
        else
            minChangeCounter = 0;

        return minChangeCounter / maxConsecutiveMinChange;
    }

    @Override
    public boolean apply(Algorithm input) {
        if (getPercentageCompleted(input) == 1.0)
            return true;

        previousBest = input.getBestSolution();
        return false;
    }
}
