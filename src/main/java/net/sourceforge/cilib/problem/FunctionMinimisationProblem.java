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

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * An implementation of {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} that can be used to find the minimum of
 * any {@link net.sourceforge.cilib.functions.Function}.
 * @author Edwin Peer
 */
public class FunctionMinimisationProblem extends FunctionOptimisationProblem {
    private static final long serialVersionUID = 8635875300412883576L;

    public FunctionMinimisationProblem() {
        super();
    }

    public FunctionMinimisationProblem(FunctionMinimisationProblem copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionMinimisationProblem getClone() {
        return new FunctionMinimisationProblem(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        /* Add code to enforce the boundary constraint */
        return new MinimisationFitness(function.evaluate((Vector) solution).doubleValue());
    }

    /**
     * <p>
     * Returns the error for the given solution. That is, a lower error value is returned if the
     * given solution is a better minimiser for the function.
     * </p>
     * <p>
     * The lowest possible error (corresponding to the best solution) should be 0. However, if the
     * function incorrectly reports its minimum value then it is possible for error values to be
     * negative.
     * </p>
     * @param solution The solution for which an error is saught.
     * @return The error.
     */
    @Override
    public double getError(Type solution) {
        return function.evaluate((Vector) solution).doubleValue() - function.getMinimum().doubleValue();
    }
}
