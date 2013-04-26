/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.nn.architecture.NeuralInputSource;

/**
 * Class represents a bias neuron. The neuron has no input weights and always
 * returns -1 as its activation.
 *
 */
public class BiasNeuron extends Neuron {

    public BiasNeuron() {
        setActivationFunction(null);
    }

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
