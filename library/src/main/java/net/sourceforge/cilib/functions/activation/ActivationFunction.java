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
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Activation functions are functions that are typically used within Neurons. This class provides
 * an abstraction for all functions that can be used in this manner.
 */
public interface ActivationFunction extends Function<Real, Real>, Differentiable, Cloneable {

    /**
     * Determine the gradient of the {@link ActivationFunction} at the given point.
     * @param number The <code>point</code> at which the gradient is to be determined.
     * @return The value of the gradient and the provided input.
     */
    double getGradient(double number);

    /**
     * Evaluates the point given a double (as opposed to Real). And also returns
     * a double. This increases scalability and performance in the NN code.
     * @param input the point to evaluate.
     * @return the evaluation result.
     */
    double apply(double input);

    /**
     *  Return the lowerbound for the active range of this NeuronFunction
     * @return the lowerbound
     */
    double getLowerActiveRange();

    /**
     * Return the upperbound for the active range of this NeuronFunction
     * @return the upperbound
     */
    double getUpperActiveRange();
}
