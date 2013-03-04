/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.mappingproblem;

/**
 * Represents a method for calculating the structure distance between two
 * vectors in a MappingProblem.
 *
 */
public interface DistanceMetric {
    /**
     * Gets called by CurvilinearDistEvaluator to indicate the precise problem
     * to make use of.  It is recommended that the results of the distances
     * be calculated and placed into some kind of matrix at this point to allow
     * for faster execution.
     *
     * @param prob The underlying MappingProblem.
     */
    void setMappingProblem(MappingProblem prob);

    /**
     * Gets called by CurvilinearDistEvaluator to request the distance between
     * two vectors to be returned.
     *
     * @param i The index of the "from" vector.
     * @param j The index of the "to" vector.
     *
     * @return The distance between to two vectors.
     */
    double getDistance(int i, int j);
}
