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
 * Implements the Curvilinear Component function for evaluating the
 * fitness of the Mapping problem.  (@see MappingProblem}
 *
 */
public class CurvilinearCompEvaluator implements MappingEvaluator {
    /**
     * Implements the evaluateMapping function as required by.
     *
     * @param dist The distance matrix for the generated output vectors.
     *
     * @return the fitness as a double, wrapped inside a Fitness.
     *
     */
    public Fitness evaluateMapping(Matrix dist) {
        int numvect = prob.getNumInputVectors();
        double res = 0.0;

        for(int i = 0; i < numvect; i++)
            for(int j = i + 1; j < numvect; j++) {
                double inp = prob.getDistanceInputVect(i, j);
                double tmp = inp - dist.valueAt(i, j);

                res += tmp * tmp * f(inp);
            }

        return new MinimisationFitness(new Double(res));
    }

    /**
     * The function F.  Well, that is at least the name in the assignment that
     * I'm working on.  The only description I've got of this is "Where F is
     * a decreasing function of o<sub>ij</sub>."
     *
     * This is probably another candidate for subclassing.
     *
     * @param o The value to apply the function to.
     *
     * @return A value that decreases as o increases, never reaching 0.
     *
     */
    protected double f(double o) {
//        return Math.exp(-o);
        return 10 / o;
    }

    /**
     * Called by MappingProblem.setEvaluator in order to establish
     * bi-directional communication.
     *
     * @param prob The instance of MappingProblem that is going use us.
     *
     */
    public void setMappingProblem(MappingProblem prob) {
        this.prob = prob;
    }

    private MappingProblem prob;
}
