/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.moo.criterion;

import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies.ObjectiveAssignmentStrategy;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * <p>
 * Serves as an adapter for a {@link MOOptimisationProblem} and converts it to a
 * single-objective optimisation problem by selecting one of the sub-objectives
 * as its active objective. This functionality is required by Criterion-based
 * Multi-objective algorithms where the search process focusses only on one of the
 * sub-objectives at a time or in a Multi-population, criterion-based multi-objective
 * algorithm where each sub-population focusses on a different objective. (see
 * {@link MultiPopulationCriterionBasedAlgorithm} where an
 * {@link ObjectiveAssignmentStrategy} is used to assign the different sub-objectives
 * to different sub-populations.
 * </p>
 *
 */
public class CriterionBasedMOProblemAdapter extends AbstractProblem {

    private static final long serialVersionUID = -3260431621464871352L;
    private MOOptimisationProblem problem;
    private int activeIndex;

    public CriterionBasedMOProblemAdapter(MOOptimisationProblem problem) {
        this.problem = problem;
        this.activeIndex = -1;
    }

    public CriterionBasedMOProblemAdapter(CriterionBasedMOProblemAdapter copy) {
        this.problem = copy.problem;
        this.activeIndex = copy.activeIndex;
    }

    @Override
    public CriterionBasedMOProblemAdapter getClone() {
        return new CriterionBasedMOProblemAdapter(this);
    }

    public Problem getActiveOptimisationProblem() {
        return this.problem.get(this.activeIndex);
    }

    public void setActiveOptimisationProblem(Problem problem) {
        this.activeIndex = this.problem.indexOf(problem);
    }

    public List<Problem> getOptimisationProblems() {
        return Collections.unmodifiableList(this.problem);
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        return this.problem.getFitness(this.activeIndex, solution);
    }

    @Override
    public DomainRegistry getDomain() {
        return getActiveOptimisationProblem().getDomain();
    }
}
