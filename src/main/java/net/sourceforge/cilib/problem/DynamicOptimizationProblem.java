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
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Dynamic optimization is currently only defined for maximization problems.
 *
 * @author Bennie Leonard
 */
public class DynamicOptimizationProblem extends FunctionOptimisationProblem {
    private FunctionOptimisationProblem functionOptimisationProblem;

    public DynamicOptimizationProblem() {
        functionOptimisationProblem = new FunctionMaximisationProblem();
    }

    public DynamicOptimizationProblem(FunctionOptimisationProblem copy) {
        this.functionOptimisationProblem = functionOptimisationProblem.getClone();
    }

    @Override
    public DynamicOptimizationProblem getClone() {
        return new DynamicOptimizationProblem(this);
    }

    @Override
    public void accept(ProblemVisitor visitor) {
        functionOptimisationProblem.accept(visitor);
    }

    @Override
    public int getDimension() {
        return functionOptimisationProblem.getDimension();
    }

    @Override
    public DomainRegistry getDomain() {
        return functionOptimisationProblem.getDomain();
    }

    @Override
    public String getDomainString() {
        return functionOptimisationProblem.getDomainString();
    }

    @Override
    public Function<Vector, ? extends Number> getFunction() {
        return functionOptimisationProblem.getFunction();
    }

    @Override
    public void setDomain(String representation) {
        functionOptimisationProblem.setDomain(representation);
    }

    @Override
    public void setFunction(Function<Vector, ? extends Number> function) {
        functionOptimisationProblem.setFunction(function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        return functionOptimisationProblem.calculateFitness(solution);
    }

    public void setFunctionOptimisationProblem(FunctionOptimisationProblem functionOptimisationProblem) {
        this.functionOptimisationProblem = functionOptimisationProblem;
    }

    public FunctionOptimisationProblem getFunctionOptimisationProblem() {
        return functionOptimisationProblem;
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
     * @param solution The solution for which an error is saught.
     * @return The error.
     */
    @Override
    public double getError(Type solution) {
        return ((DynamicFunction) functionOptimisationProblem.function).getMaximum()
                - functionOptimisationProblem.function.apply((Vector) solution).doubleValue();
    }
}
