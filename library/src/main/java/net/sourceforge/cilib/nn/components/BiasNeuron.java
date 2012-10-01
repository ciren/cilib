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
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.nn.architecture.NeuralInputSource;

/**
 * Class represents a bias neuron. The neuron has no input weights and always
 * returns -1 as its activation.
 *
 */
public class BiasNeuron extends Neuron {

    /**
     * Sets the stored activation to -1.
     * @param netInputSource the input source is not used.
     * @return the activation of -1.
     */
    @Override
    public double calculateActivation(NeuralInputSource netInputSource) {
        return this.getActivation();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public double getActivation() {
        this.setActivation(-1.0);
        return super.getActivation();
    }

    public boolean isBias() {
        return true;
    }
}
