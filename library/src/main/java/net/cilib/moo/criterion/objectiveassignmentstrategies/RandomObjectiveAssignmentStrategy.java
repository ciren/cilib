/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.moo.criterion.objectiveassignmentstrategies;

import java.util.List;

import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.math.random.generator.Rand;
import net.cilib.moo.criterion.CriterionBasedMOProblemAdapter;
import net.cilib.problem.MOOptimisationProblem;

/**
 * <p> Randomly assigns the sub-objectives of a {@link MOOptimisationProblem} to
 * different {@link PopulationBasedAlgorithm}s. </p>
 */
public class RandomObjectiveAssignmentStrategy implements ObjectiveAssignmentStrategy {

    private static final long serialVersionUID = 2421634715881142661L;

    public RandomObjectiveAssignmentStrategy() {
    }

    public RandomObjectiveAssignmentStrategy(RandomObjectiveAssignmentStrategy copy) {
    }

    @Override
    public RandomObjectiveAssignmentStrategy getClone() {
        return new RandomObjectiveAssignmentStrategy(this);
    }

    @Override
    public void assignObjectives(MOOptimisationProblem problem, List<SinglePopulationBasedAlgorithm> populations) {
        for (int i = 0; i < populations.size(); ++i) {
            int randomIndex = Rand.nextInt(problem.size());
            CriterionBasedMOProblemAdapter problemAdapter = new CriterionBasedMOProblemAdapter(problem);
            problemAdapter.setActiveOptimisationProblem(problem.get(randomIndex));
            populations.get(i).setOptimisationProblem(problemAdapter);
        }
    }
}
