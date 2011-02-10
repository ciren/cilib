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
package net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * Used by {@link MultiPopulationCriterionBasedAlgorithm} to assign the different sub-objectives
 * in a {@link MOOptimisationProblem} to specific {@link PopulationBasedAlgorithm}s.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public interface ObjectiveAssignmentStrategy extends Cloneable {

    @Override
    public ObjectiveAssignmentStrategy getClone();

    public void assignObjectives(MOOptimisationProblem problem, List<PopulationBasedAlgorithm> populations);
}
