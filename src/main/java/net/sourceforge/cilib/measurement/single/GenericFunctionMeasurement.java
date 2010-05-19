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
package net.sourceforge.cilib.measurement.single;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @param <F> The "from" type.
 * @param <T> The "to" type.
 * @author Theuns Cloete
 */
public class GenericFunctionMeasurement<F, T> implements Measurement<Real> {

    private static final long serialVersionUID = 3301062975775598397L;
    private Function<Vector, Double> function = null;

    /**
     * Create a new instance of {@linkplain GenericFunctionMeasurement}.
     */
    public GenericFunctionMeasurement() {
        function = null;
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public GenericFunctionMeasurement(GenericFunctionMeasurement copy) {
        function = copy.function;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericFunctionMeasurement getClone() {
        return new GenericFunctionMeasurement(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "R";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Preconditions.checkNotNull(function, "The function that should be evaluated has not been set");
        Vector vector = (Vector) algorithm.getBestSolution().getPosition();
        return new Real(function.apply(vector));
    }

    /**
     * Get the set function.
     * @return The contained function.
     */
    public Function<Vector, Double> getFunction() {
        return function;
    }

    /**
     * Set the function.
     * @param f The value to set.
     */
    public void setFunction(Function<Vector, Double> f) {
        function = f;
    }
}
