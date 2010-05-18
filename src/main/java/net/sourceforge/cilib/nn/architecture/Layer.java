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
package net.sourceforge.cilib.nn.architecture;

import java.util.ArrayList;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class represents a layer in a neural network, therefore it simply extends
 * an ArrayList<Neuron> . It also implements the {@link NeuralInputSource}
 * interface and can therefore be used as input for a neuron.
 * @author andrich
 */
public class Layer extends ArrayList<Neuron> implements NeuralInputSource {

    protected boolean bias;

    /**
     * Gets the neural input of this layer by getting the activation of the
     * neuron at the relevant index.
     * @param index {@inheritDoc }
     * @return the activation of the neuron at the given index.
     */
    @Override
    public double getNeuralInput(int index) {
        return this.get(index).getActivation();
    }

    /**
     * Gets the activations of the neurons in this layer as a {@link Vector}
     * @return the activations of the neurons in this layer.
     */
    public Vector getActivations() {
        Vector v = new Vector();
        for (Neuron neuron : this) {
            v.add(Real.valueOf(neuron.getActivation()));
        }
        return v;
    }

    /**
     * Whether the layer has a bias neuron.
     * @return whether the layer has a bias neuron.
     */
    public boolean isBias() {
        return bias;
    }

    /**
     * Sets whether the layer has a bias neuron.
     * @param bias whether the layer has a bias neuron.
     */
    public void setBias(boolean bias) {
        this.bias = bias;
    }
}
