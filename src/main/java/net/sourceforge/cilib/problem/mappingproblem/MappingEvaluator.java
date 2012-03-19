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
