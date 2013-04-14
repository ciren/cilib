/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture;

import java.util.ArrayList;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Class represents a layer in a neural network, therefore it simply extends
 * an ArrayList<Neuron> . It also implements the {@link NeuralInputSource}
 * interface and can therefore be used as input for a neuron.
 */
public class Layer extends ArrayList<Neuron> implements NeuralInputSource, Cloneable {

    protected boolean bias;

    public Layer() {
        bias = false;
    }

    public Layer(Layer rhs) {
        bias = rhs.bias;

        for (Neuron curNeuron : rhs) {
            add(curNeuron.getClone());
        }
    }

    public Layer getClone() {
        return new Layer(this);
    }

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
        Vector.Builder v = Vector.newBuilder();
        
        for (Neuron neuron : this) {
            v.add(neuron.getActivation());
        }
        
        return v.build();
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

	/**
     * {@inheritDoc }
     */
    @Override
	public Neuron getNeuron(int index) {
		return this.get(index);
    }
}
