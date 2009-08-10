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

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.container.Matrix;

/**
 * Class that actually evaluates a given Mapping. This is to allow using
 * the same evaluator for different mapping mechanisms in an easy manner.
 *
 * @author jkroon
 */
public interface MappingEvaluator {
    /**
     * Get called in order to perform an evaluation.
     *
     * @param dist The distance matrix for the output vectors.
     *
     * @return An instance of Fitness indicating the Fitness of this mapping.
     *
     * @author jkroon
     */
    public Fitness evaluateMapping(Matrix dist);

    /**
     * Gets called when attached to a MappingProblem.  This is to allow
     * one to call back to the problem in order to find additional info,
     * such as the distance between vectors in the input.
     *
     * @param prob The instance of MappingProblem that is going to use the evaluator.
     *
     * @author jkroon
     */
    public void setMappingProblem(MappingProblem prob);

}
