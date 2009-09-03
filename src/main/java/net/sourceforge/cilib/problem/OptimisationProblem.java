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
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * <p>
 * Optimisation problems are characterized by a domain that specifies the search space and
 * a fitness given a potential solution. This interface ensures that an
 * {@linkplain net.sourceforge.cilib.algorithm.OptimisationAlgorithm optimization algorithm} has
 * all the information it needs to find a solution to a given optimisation problem. In addition, it is the
 * responsibility of an optimisation problem to keep track of the number of times the fitness has
 * been evaluated.
 * </p>
 * <p>All optimisation problems must implement this interface.</p>
 *
 * @author  Edwin Peer
 */
public interface OptimisationProblem extends Problem {

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationProblem getClone();

    /**
     * Returns the fitness of a potential solution to this problem. The solution object is described
     * by the domain of this problem, see {@link #getDomain()}. An instance of
     * {@link net.sourceforge.cilib.problem.InferiorFitness} should be returned if the solution
     * falls outside the search space of this problem.
     *
     * @param solution The potential solution found by the optimisation algorithm.
     * @return The fitness of the solution.
     */
    public Fitness getFitness(Type solution);

    /**
     * Returns the number of times the underlying fitness function has been evaluated.
     *
     * @return The number fitness evaluations.
     */
    public int getFitnessEvaluations();

    /**
     * Returns the domain component that describes the search space for this problem.
     *
     * @return A {@link net.sourceforge.cilib.type.DomainRegistry} object representing the search space.
     */
    public DomainRegistry getDomain();


    /**
     * Get the associated {@link net.sourceforge.cilib.problem.dataset.DataSetBuilder}.
     * @return The currently associated {@link net.sourceforge.cilib.problem.dataset.DataSetBuilder}.
     */
    public DataSetBuilder getDataSetBuilder();


    /**
     * Set the {@link net.sourceforge.cilib.problem.dataset.DataSetBuilder} for this
     * {@link net.sourceforge.cilib.problem.OptimisationProblem optimistion problem}.
     * @param dataSetBuilder The {@link net.sourceforge.cilib.problem.dataset.DataSetBuilder}
     *                       to be set on the current {@link net.sourceforge.cilib.problem.OptimisationProblem}.
     */
    public void setDataSetBuilder(DataSetBuilder dataSetBuilder);

    /**
     * Accept the provided {@link net.sourceforge.cilib.container.visitor.Visitor} and
     * perform the {@link net.sourceforge.cilib.container.visitor.Visitor#visit(Object)} method.
     * @param visitor The visitor which has operations to perform.
     */
    public void accept(ProblemVisitor visitor);

}
