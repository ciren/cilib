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
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Type;

/**
 * Give the current value of the global optimum of the
 * function.
 *
 *
 */
public class GlobalMaximum implements Measurement {

    private static final long serialVersionUID = 2658868675629949642L;

    public GlobalMaximum() {
    }

    public GlobalMaximum(GlobalMaximum rhs) {
    }

    @Override
    public GlobalMaximum clone() {
        return new GlobalMaximum(this);
    }

    @Override
    public String getDomain() {
        return "R";
    }

    @Override
    public Type getValue(Algorithm algorithm) {
//        FunctionOptimisationProblem problem = (FunctionOptimisationProblem) algorithm.getOptimisationProblem();
//        double value = problem.getFunction().getMaximum().doubleValue();
//        return new Real(value);
        throw new UnsupportedOperationException("Implementation is required... this is not correct");
    }

    @Override
    public Measurement getClone() {
        return new GlobalMaximum(this);
    }
}
