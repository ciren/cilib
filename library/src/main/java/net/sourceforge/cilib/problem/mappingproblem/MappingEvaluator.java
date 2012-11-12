/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.mappingproblem;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.Matrix;

/**
 * Class that actually evaluates a given Mapping. This is to allow using
 * the same evaluator for different mapping mechanisms in an easy manner.
 *
 */
public interface MappingEvaluator {
    /**
     * Get called in order to perform an evaluation.
     *
     * @param dist The distance matrix for the output vectors.
     *
     * @return An instance of Fitness indicating the Fitness of this mapping.
     *
     */
    Fitness evaluateMapping(Matrix dist);

    /**
     * Gets called when attached to a MappingProblem.  This is to allow
     * one to call back to the problem in order to find additional info,
     * such as the distance between vectors in the input.
     *
     * @param prob The instance of MappingProblem that is going to use the evaluator.
     *
     */
    void setMappingProblem(MappingProblem prob);

}
