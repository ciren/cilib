/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem.mappingproblem;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Matrix;

/**
 * Implements the Curvilinear Distance function for evaluating the
 * fitness of the Mapping problem.  (@see MappingProblem}
 *
 * @author jkroon
 */
public class CurvilinearDistEvaluator implements MappingEvaluator {
    /**
     * Implements the evaluateMapping function as required by.
     *
     * @param d The distrance matrix for the generated output vectors.
     *
     * @return the fitness as a double, wrapped inside a Fitness.
     *
     * @author jkroon
     */
    public Fitness evaluateMapping(Matrix d) {
        int numvect = prob.getNumInputVectors();
        double res = 0.0;

        for(int i = 0; i < numvect; i++)
            for(int j = i + 1; j < numvect; j++) {
                double inp = dist.getDistance(i, j);
                double tmp = inp - d.valueAt(i, j);

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
     * @author jkroon
     */
    protected double f(double o) {
//        return Math.exp(-o);
        return 10 / o;
    }

    /**
     * Called by MappingProblem.setEvaluator in order to establisg
     * bi-directional communication.
     *
     * @param prob The instance of MappingProblem that is going use us.
     *
     * @author jkroon
     */
    public void setMappingProblem(MappingProblem prob) {
        this.prob = prob;
        if(dist != null)
            dist.setMappingProblem(prob);
    }

    /**
     * Called by XML factory to set the DistranceMetric to use.
     *
     * @param distanceMetric The DistanceMetric to use
     *
     * @author jkroon
     */
    public void setDistanceMetric(DistanceMetric distanceMetric) {
        dist = distanceMetric;
        if (prob != null)
            dist.setMappingProblem(prob);
    }

    /**
     * Can be used to retrieve the DistanceMetric to use.
     *
     * @return The DistanceMetric currently in use.
     *
     * @author jkroon
     */
    public DistanceMetric getDistanceMetric() {
        return dist;
    }

    private DistanceMetric dist;
    private MappingProblem prob;
}
