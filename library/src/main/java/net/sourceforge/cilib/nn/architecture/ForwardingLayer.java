/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
     * @return the of this layer
     */
    @Override
    public int size() {
        return this.getSourceSize() + super.size();
    }

	/**
     * {@inheritDoc }
     */
	@Override
    public Neuron getNeuron(int index) {
        if (index < sourceSize) {
            return this.source.getNeuron(index);
        }
        return super.getNeuron(index - sourceSize);
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
