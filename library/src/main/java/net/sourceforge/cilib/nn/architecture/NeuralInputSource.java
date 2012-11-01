/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture;

import net.sourceforge.cilib.nn.components.Neuron;

/**
 * An interface to a neural input source i.e. a source from which a neuron
 * can calculate its activations. This abstraction is necessary for avoiding
 * special cases such as when the feeding source is a pattern, or two layers
 * instead of one.
 */
public interface NeuralInputSource {

    /**
     * Gets the neural input at the specified index.
     * @param index the index of the input to retrieve.
     * @return the neural input at the specified index.
     */
    double getNeuralInput(int index);

    /**
     * Gets the size of the neural input source.
     * @return the size of the neural input source.
     */
    int size();

	/**
	 * Gets the neuron at the specified index
     * @param index the index of the input to retrieve.
     * @return the neuron at the specified index.
	 */
	Neuron getNeuron(int index);
}
