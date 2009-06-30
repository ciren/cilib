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
package net.sourceforge.cilib.problem.mappingproblem;

/**
 * Represents a method for calculating the structure distance between two
 * vectors in a MappingProblem.  This specific one simply assumes that we
 * are looking at some "linear" structure and that all input vectors logically
 * follows on one another.  This can sometimes be the case, but I don't see
 * many situations where this will make a hell of a lot of sense...
 *
 * @author jkroon
 */
public class LinearDistanceMetric implements DistanceMetric {
    /**
     * Gets called by CurvilinearDistEvaluator to indicate the precice problem
     * to make use of.  It is recommended that the results of the distances
     * be calculated and placed into some kind of matrix at this point to allow
     * for faster execution.
     *
     * @param prob The underlying MappingProblem.
     */
    public void setMappingProblem(MappingProblem prob) {
        int nvect = prob.getNumInputVectors();
        distanceOffsets = new double[nvect];
        double currentOffset = 0.0;
        distanceOffsets[0] = 0.0;

        for(int i = 1; i < nvect; i++) {
            currentOffset += prob.getDistanceInputVect(i - 1, i);
            distanceOffsets[i] = currentOffset;
        }

        totalDistance = currentOffset + prob.getDistanceInputVect(nvect - 1, 0);
    }

    /**
     * Gets called by CurvilinearDistEvaluator to request the distance between
     * two vectors to be returned.
     *
     * @param i The index of the "from" vector.
     * @param j The index of the "to" vector.
     *
     * @return The distance between to two vectors.
     */
    public double getDistance(int i, int j) {
        double distance = distanceOffsets[j] - distanceOffsets[i];

        // if(j < i)
        if(distance < 0.0) {
            distance = -distance;
        }

        if(doloop && totalDistance - distance < distance) {
            distance = totalDistance - distance;
        }

        return distance;
    }

    /**
     * Sets the looping status.  If the looping status is active it is assumed
     * that we are working with some looping structure, ie, the last point goes
     * back to the first.  In this case there are two possible directions from
     * one vector to another, the shortest one is returned.
     *
     * @param loopstatus What the loopstatus should be (true/false).
     */
    public void setLoopStatus(boolean loopstatus) {
        doloop = loopstatus;
    }

    /**
     * Retrieves the looping status.  {@link setLoopStatus}
     *
     * @return The current loop status.
     */
    public boolean getLoopStatus() {
        return doloop;
    }

    private double []distanceOffsets;
    private double totalDistance;
    private boolean doloop;
}
