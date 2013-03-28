/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.mappingproblem;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Matrix;

/**
 * Implements the Kruskal stress function for evaluating the fitness of the
 * {@link MappingProblem}.
 */
public class KruskalEvaluator implements MappingEvaluator {
    /**
     * Implements the evaluateMapping function.
     *
     * @param dist The distance matrix for the generated output vectors.
     * @return the fitness as a double, wrapped inside a {@link Fitness}.
     */
    public Fitness evaluateMapping(Matrix dist) {
        double above = 0.0;
        double below = 0.0;

        int numvect = prob.getNumInputVectors();

        for(int i = 0; i < numvect; i++) {
            for(int j = i + 1; j < numvect; j++) {
                double inp_dist = prob.getDistanceInputVect(i, j);
                double tmp = inp_dist - dist.valueAt(i, j);

                above += tmp * tmp;
//                below += inp_dist * inp_dist;
                below += dist.valueAt(i, j) * dist.valueAt(i, j);
            }
        }

        return new MinimisationFitness(new Double(Math.sqrt(above / below)));
    }

    public void setMappingProblem(MappingProblem prob) {
        this.prob = prob;
    }

    private MappingProblem prob;
}
