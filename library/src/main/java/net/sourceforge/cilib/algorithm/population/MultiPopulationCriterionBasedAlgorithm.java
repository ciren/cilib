/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.population;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies.KnowledgeTransferStrategy;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies.ObjectiveAssignmentStrategy;
import net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies.SequentialObjectiveAssignmentStrategy;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 * <p>
 * Generic class that represents the case where a Criterion-based {@link MultiPopulationBasedAlgorithm}
 * (like VEPSO) is used to solve a Multi-objective problem. The class makes use of an
 * {@link ObjectiveAssignmentStrategy} to assign the {@link MOOptimisationProblem}'s sub-objectives to
 * the different sub-populations. Some {@link KnowledgeTransferStrategy} mechanism is needed to share
 * the knowledge gained during the search among the different sub-populations.
 * </p>
 *
 */
public class MultiPopulationCriterionBasedAlgorithm extends MultiPopulationBasedAlgorithm {

    private static final long serialVersionUID = -4184467214937409629L;
    private ObjectiveAssignmentStrategy objectiveAssignmentStrategy;

    public MultiPopulationCriterionBasedAlgorithm() {
        super();
        this.objectiveAssignmentStrategy = new SequentialObjectiveAssignmentStrategy();
    }

    public MultiPopulationCriterionBasedAlgorithm(MultiPopulationCriterionBasedAlgorithm copy) {
        super(copy);
        this.objectiveAssignmentStrategy = copy.objectiveAssignmentStrategy.getClone();
    }

    @Override
    public MultiPopulationCriterionBasedAlgorithm getClone() {
        return new MultiPopulationCriterionBasedAlgorithm(this);
    }

    public void setObjectiveAssignmentStrategy(ObjectiveAssignmentStrategy objectiveAssignmentStrategy) {
        this.objectiveAssignmentStrategy = objectiveAssignmentStrategy;
    }

    public ObjectiveAssignmentStrategy getObjectiveAssignmentStrategy() {
        return this.objectiveAssignmentStrategy;
    }

    @Override
    public void algorithmInitialisation() {
        this.objectiveAssignmentStrategy.assignObjectives((MOOptimisationProblem) this.getOptimisationProblem(),
                this.subPopulationsAlgorithms);
        for (Algorithm algorithm : this) {
            algorithm.performInitialisation();
        }
    }

    @Override
    protected void algorithmIteration() {
        for (Algorithm algorithm : this) {
            algorithm.performIteration();
        }
    }

    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("This algorithm is only applicable to multi-objective problems," +
                "and thus only returns a Paretto optimal set.");
    }

    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        solutions.addAll(Archive.Provider.get());
        return solutions;
    }
}
