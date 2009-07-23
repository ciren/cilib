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
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.functions.AbstractFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Activation functions are functions that are typically used within Neurons. This class provides
 * an abstraction for all functions that can be used in this manner.
 */
public abstract class ActivationFunction extends AbstractFunction<Vector, Double> implements Differentiable {
    private static final long serialVersionUID = 4692200308338537909L;

    /**
     * Determine the value of the {@link ActivationFunction} at the provided <code>point</code>.
     * The provided <code>point</code> is simply a {@linkplain Number} that is provided as input.
     * @param number The input value.
     * @return The evaluation of the {@link ActivationFunction}, given <code>number</code> as
     *         input.
     */
    public abstract Double evaluate(Number number);

    /**
     * Determine the gradient of the {@link ActivationFunction} at the given point.
     * @param number The <code>point</code> at which the gradient is to be determined.
     * @return The value of the gradient and the provided input.
     */
    public abstract Double getGradient(Number number);

}
