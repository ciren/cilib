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

import net.sourceforge.cilib.functions.DynamicFunction;
import net.sourceforge.cilib.problem.changestrategy.ChangeStrategy;
import net.sourceforge.cilib.problem.changestrategy.IterationBasedSingleChangeStrategy;
import net.sourceforge.cilib.problem.objective.Maximise;
import net.sourceforge.cilib.problem.objective.Objective;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Type;

public class DynamicOptimizationProblem extends AbstractProblem {

    protected DynamicFunction<Type, ? extends Number> function;
    protected Objective objective;
    protected ChangeStrategy changeStrategy;

    public DynamicOptimizationProblem() {
        this.objective = new Maximise();
        this.changeStrategy = new IterationBasedSingleChangeStrategy(1);
    }

    public DynamicOptimizationProblem(DynamicOptimizationProblem copy) {
        super(copy);
        this.function = copy.function;
        this.objective = copy.objective;
        this.changeStrategy = copy.changeStrategy;
    }

    @Override
    public DynamicOptimizationProblem getClone() {
        return new DynamicOptimizationProblem(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        if (changeStrategy.shouldApply(this)) {
            function.changeEnvironment();
        }

        return objective.evaluate(function.apply(solution).doubleValue());
    }

    /**
     * <p>
     * Returns the error for the given solution. That is, a lower error value is returned if the
     * given solution is a better maximiser for the function. This method assumes that the
     * {@link FunctionOptimisationProblem} contains a {@link DynamicFunction}.
     * </p>
     * <p>
     * The lowest possible error (corresponding to the best solution) should be 0. However, if the
     * function incorrectly reports its maximum value then it is possible for error values to be
     * negative.
     * </p>
     *
     * @param solution The solution for which an error is sought.
     * @return The error.
     */
    public double getError(Type solution) {
        return function.getOptimum().doubleValue() - function.apply(solution).doubleValue();
    }

    public DynamicFunction<Type, ? extends Number> getFunction() {
        return function;
    }

    public void setFunction(DynamicFunction<Type, ? extends Number> function) {
        this.function = function;
    }
}
