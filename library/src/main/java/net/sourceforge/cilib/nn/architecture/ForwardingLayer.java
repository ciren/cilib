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

import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implements a layer that forwards another {@link NeuralInputSource} as its
 * own NeuralInputSource. In terms of index, the other NeuralInputSource's input
 * is returned before indexing into the ForwardingLayer's neurons. As such
 * this class can be used as an input layer, forwarding from a {@link PatternInputSource}
 */
public class ForwardingLayer extends Layer {

    private NeuralInputSource source;
    private int sourceSize;

    /**
     * Returns the input of the source if the index is smaller than the source's
     * size, otherwise returns the activation of the neuron contained in this layer.
     * @param index {@inheritDoc }
     * @return the neural input source.
     */
    @Override
    public double getNeuralInput(int index) {
        if (index < sourceSize) {
            return this.source.getNeuralInput(index);
        }
        return super.get(index - sourceSize).getActivation();
    }

    /**
     * Gets the activations of this layer by first getting the source's input and
     * then adding the layer's own neuron activations.
     * @return this layer's activations.
     */
    @Override
    public Vector getActivations() {
        Vector.Builder activations = Vector.newBuilder();
        
        for (int i = 0; i < this.size(); i++) {
            activations.add(getNeuralInput(i));
        }
        
        return activations.build();
    }

    /**
     * Gets the size of this layer as the sum of the source's and its own.
     * @return
     */
    @Override
    public int size() {
        return this.getSourceSize() + super.size();
    }

	/**
     * {@inheritDoc }
     */
	@Override
    public Neuron get(int index) {
        if (index < sourceSize) {
            return this.source.get(index);
        }
        return super.get(index - sourceSize);
    }

    /**
     * Gets the source size.
     * @return the source size.
     */
    public int getSourceSize() {
        return sourceSize;
    }

    /**
     * Set the source size.
     * @param sourceSize the source size.
     */
    public void setSourceSize(int sourceSize) {
        this.sourceSize = sourceSize;
    }

    /**
     * Gets the source that is forwarded.
     * @return the source being forwarded.
     */
    public NeuralInputSource getSource() {
        return source;
    }

    /**
     * Sets the source to forward.
     * @param source the source to forward.
     */
    public void setSource(NeuralInputSource source) {
        this.source = source;
        this.setSourceSize(this.source.size());
    }
}
