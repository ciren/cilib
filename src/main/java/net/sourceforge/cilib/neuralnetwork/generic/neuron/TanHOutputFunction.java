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
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * Hyperbollic Tangent Function.
 */
public class TanHOutputFunction implements NeuronFunction {

    private static final long serialVersionUID = 6996330036408791132L;

    public TanHOutputFunction() {
    }

    /**
     * {@inheritDoc}
     */
    public Type computeDerivativeAtPos(Type pos) {
        Real value = (Real) pos;
        double result = 1 - Math.tanh(value.getReal())*Math.tanh(value.getReal());
        return new Real(result);
    }

    /**
     * {@inheritDoc}
     */
    public Type computeDerivativeUsingLastOutput(Type lastOut) {
        Real value = (Real) lastOut;
        double result = 1 - Math.tanh(value.getReal())*Math.tanh(value.getReal());
        return new Real(result);
    }

    /**
     * {@inheritDoc}
     */
    public Type computeFunction(Type in) {
        double a = Math.exp(((Real)in).getReal());
        double b = Math.exp(-((Real)in).getReal());
        return new Real(((a-b)/(a+b)));
    }

    /**
     * {@inheritDoc}
     */
    public TanHOutputFunction getClone() {
        return new TanHOutputFunction();
    }

    /**
    * {@inheritDoc}
    * The active range is -Sqrt(3) - Sqrt(3), and Sqrt(3) = 1.732050808
    */
    public double getLowerActiveRange() {
        return -1.732050808;
    }

    /**
    * {@inheritDoc}
    */
    public double getUpperActiveRange() {
        return 1.732050808;
    }

}
