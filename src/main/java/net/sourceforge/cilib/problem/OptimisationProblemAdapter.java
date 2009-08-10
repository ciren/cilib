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

import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.cilib.problem.changestrategy.ChangeStrategy;
import net.sourceforge.cilib.problem.changestrategy.NoChangeStrategy;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.types.Type;

/**
 * <p>
 * This is a convenience class that keeps track of the number of fitness evaluations. This class can
 * be extend instead of implementing {@link net.sourceforge.cilib.problem.OptimisationProblem} directly.
 * </p>
 * <p>
 * The contract of returning an instance of {@link net.sourceforge.cilib.problem.InferiorFitness} for
 * solutions outside the problem search space is implemented by {@link #getFitness(Type, boolean)}
 * </p>
 * @author Edwin Peer
 */
public abstract class OptimisationProblemAdapter implements OptimisationProblem {
    private static final long serialVersionUID = -5008516277429476778L;

    protected AtomicInteger fitnessEvaluations;
    protected DataSetBuilder dataSetBuilder;
    private ChangeStrategy changeStrategy;

    public OptimisationProblemAdapter() {
        fitnessEvaluations = new AtomicInteger(0);
        changeStrategy = new NoChangeStrategy();
    }

    public OptimisationProblemAdapter(OptimisationProblemAdapter copy) {
        fitnessEvaluations = new AtomicInteger(copy.fitnessEvaluations.get());
        if(copy.dataSetBuilder != null)
            dataSetBuilder = copy.dataSetBuilder.getClone();
    }

    @Override
    public abstract OptimisationProblemAdapter getClone();

    /**
     * Determine the {@code Fitness} of the current {@link Problem} instance
     * based on the provided {@code solution}.
     * @param solution The {@link net.sourceforge.cilib.type.types.Type} representing the candidate solution.
     * @return The {@link Fitness} of the {@code solution} in the current {@linkplain Problem}.
     * @see OptimisationProblemAdapter#getFitness(Type, boolean)
     */
    protected abstract Fitness calculateFitness(Type solution);

    /**
     * {@inheritDoc}
     */
    public final Fitness getFitness(Type solution) {
        fitnessEvaluations.incrementAndGet();

        if (this.changeStrategy.shouldApply(this))
            changeEnvironment();

        return calculateFitness(solution);
    }

    /**
     * {@inheritDoc}
     */
    public final int getFitnessEvaluations() {
        return fitnessEvaluations.get();
    }

    /**
     * {@inheritDoc}
     */
    public DataSetBuilder getDataSetBuilder() {
        return this.dataSetBuilder;
    }

    /**
     * {@inheritDoc}
     */
    public void setDataSetBuilder(DataSetBuilder dsb) {
        dataSetBuilder = dsb;
    }

    /**
     * {@inheritDoc}
     */
    public void accept(ProblemVisitor visitor) {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * {@inheritDoc}
     */
    public void changeEnvironment() {
        throw new UnsupportedOperationException("Problems are static by default. Dynamic problems should override this method");
    }

    /**
     * Get the current problem change strategy.
     * @return The current {@link net.sourceforge.cilib.problem.changestrategy.ChangeStrategy}.
     */
    public ChangeStrategy getChangeStrategy() {
        return changeStrategy;
    }

    /**
     * Set the {@link net.sourceforge.cilib.problem.changestrategy.ChangeStrategy} for this problem.
     * @param changeStrategy The {@link net.sourceforge.cilib.problem.changestrategy.ChangeStrategy} to set.
     */
    public void setChangeStrategy(ChangeStrategy changeStrategy) {
        this.changeStrategy = changeStrategy;
    }

}
