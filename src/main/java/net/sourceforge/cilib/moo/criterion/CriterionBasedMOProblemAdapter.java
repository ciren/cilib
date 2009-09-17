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
package net.sourceforge.cilib.moo.criterion;

import java.util.Collections;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies.ObjectiveAssignmentStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
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
 * @author Wiehann Matthysen
 */
public class CriterionBasedMOProblemAdapter extends OptimisationProblemAdapter {

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

    public OptimisationProblem getActiveOptimisationProblem() {
        return this.problem.get(this.activeIndex);
    }

    public void setActiveOptimisationProblem(OptimisationProblem problem) {
        this.activeIndex = this.problem.indexOf(problem);
    }

    public List<OptimisationProblem> getOptimisationProblems() {
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
