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

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Matrix;

/**
 * Implements the Kruskal stress function for evaluating the fitness of the MappingProblem.
 *
 */
public class KruskalEvaluator implements MappingEvaluator {
    /**
     * Implements the evaluateMapping function as required by {@see NonlinearMappingProblem}.
     *
     * @param dist The distrance matrix for the generated output vectors.
     *
     * @return the fitness as a double, wrapped inside a Fitness.
     *
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

    /**
     *
     */
    public void setMappingProblem(MappingProblem prob) {
        this.prob = prob;
    }

    private MappingProblem prob;
}
