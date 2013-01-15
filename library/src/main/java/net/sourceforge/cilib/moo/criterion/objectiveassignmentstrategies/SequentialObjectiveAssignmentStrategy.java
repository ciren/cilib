/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
