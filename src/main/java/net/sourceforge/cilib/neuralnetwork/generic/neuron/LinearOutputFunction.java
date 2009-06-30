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
 * @author stefanv
 *
 */
public class LinearOutputFunction implements NeuronFunction {
    private static final long serialVersionUID = -4242396710596279416L;

    public LinearOutputFunction() {
        super();
    }


    public Type computeFunction(Type in) {
        return new Real(((Real) in).getReal());
    }


    public Type computeDerivativeAtPos(Type pos) {
        return new Real(1.0);
    }


    public Type computeDerivativeUsingLastOutput(Type lastOut) {
        return new Real(1.0);
    }

    /**
     * {@inheritDoc}
     */
    public LinearOutputFunction getClone() {
        return new LinearOutputFunction();
    }

    /**
     * {@inheritDoc}
     */
    public double getLowerActiveRange() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperActiveRange() {
        return Double.POSITIVE_INFINITY;
    }

}
