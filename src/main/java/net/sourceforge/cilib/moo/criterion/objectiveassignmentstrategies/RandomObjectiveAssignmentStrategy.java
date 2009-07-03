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
import java.util.Random;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.moo.criterion.CriterionBasedMOProblemAdapter;
import net.sourceforge.cilib.problem.MOOptimisationProblem;

/**
 * <p>
 * Randomly assigns the sub-objectives of a {@link MOOptimisationProblem} to different
 * {@link PopulationBasedAlgorithm}s.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class RandomObjectiveAssignmentStrategy implements ObjectiveAssignmentStrategy {
	private static final long serialVersionUID = 2421634715881142661L;

	public RandomObjectiveAssignmentStrategy() { }

	public RandomObjectiveAssignmentStrategy(RandomObjectiveAssignmentStrategy copy) { }

	@Override
	public RandomObjectiveAssignmentStrategy getClone() {
		return new RandomObjectiveAssignmentStrategy(this);
	}

	@Override
	public void assignObjectives(MOOptimisationProblem problem, List<PopulationBasedAlgorithm> populations) {
		Random generator = new MersenneTwister();
		for (int i = 0; i < populations.size(); ++i) {
			int randomIndex = generator.nextInt(problem.size());
			CriterionBasedMOProblemAdapter problemAdapter = new CriterionBasedMOProblemAdapter(problem);
			problemAdapter.setActiveOptimisationProblem(problem.get(randomIndex));
			populations.get(i).setOptimisationProblem(problemAdapter);
		}
	}
}
