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
package net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.moo.criterion.CriterionBasedMOProblemAdapter;
import net.sourceforge.cilib.problem.MOOptimisationProblem;

/**
 * <p>
 * Sequentially assigns the sub-objectives of a {@link MOOptimisationProblem} to different
 * {@link PopulationBasedAlgorithm} instances. Thus, sub-objective 1 will be assigned to
 * {@code PopulationBasedAlgorithm} 1, sub-objective 2 will be assigned to
 * {@code PopulationBasedAlgorithm} 2 and so on. If there are more {@code PopulationBasedAlgorithm}s
 * than the number of sub-objectives then the assignment process will wrap and start from the
 * first sub-objective again until every {@code PopulationBasedAlgorithm} is assigned a
 * sub-objective.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class SequentialObjectiveAssignmentStrategy implements ObjectiveAssignmentStrategy {

    private static final long serialVersionUID = -4851566668590841596L;

    public SequentialObjectiveAssignmentStrategy() {
    }

    public SequentialObjectiveAssignmentStrategy(SequentialObjectiveAssignmentStrategy copy) {
    }

    @Override
    public SequentialObjectiveAssignmentStrategy getClone() {
        return new SequentialObjectiveAssignmentStrategy(this);
    }

    @Override
    public void assignObjectives(MOOptimisationProblem problem, List<PopulationBasedAlgorithm> populations) {
        if (problem.size() > populations.size()) {
            throw new IllegalArgumentException("There are more objectives than the number of populations required to solve this multi-objective problem.");
        }
        for (int i = 0; i < populations.size(); ++i) {
            CriterionBasedMOProblemAdapter problemAdapter = new CriterionBasedMOProblemAdapter(problem);
            problemAdapter.setActiveOptimisationProblem(problem.get(i % problem.size()));
            populations.get(i).setOptimisationProblem(problemAdapter);
        }
    }
}
