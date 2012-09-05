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
package net.sourceforge.cilib.problem;

import java.util.concurrent.atomic.AtomicInteger;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.objective.Minimise;
import net.sourceforge.cilib.problem.objective.Objective;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
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
 */
public abstract class AbstractProblem implements Problem {

    private static final long serialVersionUID = -5008516277429476778L;

    protected AtomicInteger fitnessEvaluations;
    protected DataSetBuilder dataSetBuilder;
    protected DomainRegistry domainRegistry;
    protected Objective objective;

    protected AbstractProblem() {
        this.fitnessEvaluations = new AtomicInteger(0);
        this.domainRegistry = new StringBasedDomainRegistry();
        this.objective = new Minimise();
    }

    protected AbstractProblem(AbstractProblem copy) {
        this.fitnessEvaluations = new AtomicInteger(copy.fitnessEvaluations.get());
        this.domainRegistry = copy.domainRegistry.getClone();
        this.objective = copy.objective;

        if (copy.dataSetBuilder != null) {
            this.dataSetBuilder = copy.dataSetBuilder.getClone();
        }
    }

    @Override
    public abstract AbstractProblem getClone();

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
    @Override
    public final Fitness getFitness(Type solution) {
        fitnessEvaluations.incrementAndGet();

        return calculateFitness(solution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getFitnessEvaluations() {
        return fitnessEvaluations.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSetBuilder getDataSetBuilder() {
        return this.dataSetBuilder;
    }

    /**
     * {@inheritDoc}
     * @param dsb
     */
    @Override
    public void setDataSetBuilder(DataSetBuilder dsb) {
        dataSetBuilder = dsb;
    }

    @Override
    public DomainRegistry getDomain() {
        if (domainRegistry.getDomainString() == null) {
            throw new IllegalStateException("Domain has not been defined. Please define domain for function optimization.");
        }
        return domainRegistry;
    }

    @Override
    public void setDomain(String domain) {
        this.domainRegistry.setDomainString(domain);
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Objective getObjective() {
        return objective;
    }
}
