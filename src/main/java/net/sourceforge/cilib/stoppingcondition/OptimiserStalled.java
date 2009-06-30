/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.ManhattanDistanceMeasure;

/**
 * used to check if optimisation algorithm has stalled. still a very crude implementation. if the
 * distance from the current best is less then the specified minimum for more then the specified
 * maximum consecutive minimum change, then the algorithm is assumed to have stalled.
 * @author frans
 */
public class OptimiserStalled implements StoppingCondition {
    private static final long serialVersionUID = 4017249915571841835L;

    protected double minChange;
    protected int maxConsecutiveMinChange;
    protected int minChangeCounter;

    DistanceMeasure distMeasure;
    Algorithm algorithm;
    OptimisationSolution previousBest;

    /**
     * Creates a new instance of OptimiserStalled.
     */
    public OptimiserStalled() {
        minChange = 0.05;
        maxConsecutiveMinChange = 5;

        minChangeCounter = 0;
        distMeasure = new ManhattanDistanceMeasure();
    }

    /**
     * Copy constructor.
     * @param copy The instance to create the copy from.
     */
    public OptimiserStalled(OptimiserStalled copy) {
        this.minChange = copy.minChange;
        this.maxConsecutiveMinChange = copy.maxConsecutiveMinChange;
        this.minChangeCounter = copy.minChangeCounter;
        this.distMeasure = copy.distMeasure;
        this.algorithm = copy.algorithm;
    }

    /**
     * {@inheritDoc}
     */
    public OptimiserStalled getClone() {
        return new OptimiserStalled(this);
    }

    /**
     * {@inheritDoc}
     */
    public double getPercentageCompleted() {
        // check if this is the first iteration
        if (previousBest == null) {
            previousBest = algorithm.getBestSolution();

            return 0.0;
        }

        // get the distance between previous and current best
        // double distance = distMeasure.distance((double[])previousBest.getPosition(),
        // (double[])algorithm.getBestSolution().getPosition());
        double distance = distMeasure.distance((Vector) previousBest.getPosition(), (Vector) algorithm.getBestSolution().getPosition());

        // compare to see change
        if (distance < minChange)
            minChangeCounter++;
        else
            minChangeCounter = 0;

        return minChangeCounter / maxConsecutiveMinChange;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCompleted() {
        if (getPercentageCompleted() == 1.0)
            return true;

        previousBest = algorithm.getBestSolution();
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * sets the minimum percentage that the new best location must be from the previous.
     * @param distance The value to set.
     */
    public void setMinimumChange(double distance) {
        minChange = distance;
    }

    /**
     * sets the maximum consecutive evalutions that an algorithm can improve less then the minimum.
     * percentage. this value relates to evaluations and not iterations.
     * @param count The value to set.
     */
    public void setMaxConsecutiveMinChange(int count) {
        maxConsecutiveMinChange = count;
    }
}
