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
package net.sourceforge.cilib.algorithm.population;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies.KnowledgeTransferStrategy;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies.ObjectiveAssignmentStrategy;
import net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies.SequentialObjectiveAssignmentStrategy;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * <p>
 * Generic class that represents the case where a Criterion-based {@link MultiPopulationBasedAlgorithm}
 * (like VEPSO) is used to solve a Multi-objective problem. The class makes use of an
 * {@link ObjectiveAssignmentStrategy} to assign the {@link MOOptimisationProblem}'s sub-objectives to
 * the different sub-populations. Some {@link KnowledgeTransferStrategy} mechanism is needed to share
 * the knowledge gained during the search among the different sub-populations.
 * </p>
 *
 * @author Wiehann Matthysen
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
    public void performInitialisation() {
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
    public void performUninitialisation() {
        for (Algorithm algorithm : this) {
            algorithm.performUninitialisation();
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
