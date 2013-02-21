/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.functions.activation.ActivationFunction;
import net.sourceforge.cilib.functions.activation.Sigmoid;

/**
 * Intermediate object that encapsulates the data that specifies a layer configuration.
 * This includes its size, the activation function to use and whether it has a
 * bias neuron.
 */
public class LayerConfiguration {

    private int size;
    private ActivationFunction activationFunction;
    private boolean bias;

    /**
     * Default constructor. Default size is 0, default activation function is
     * Sigmoid and by default the layer has a bias neuron.
     */
    public LayerConfiguration() {
        this.size = 0;
        this.activationFunction = new Sigmoid();
        this.bias = true;
    }

    public LayerConfiguration(LayerConfiguration rhs) {
        this.size = rhs.size;
        this.activationFunction = rhs.activationFunction;
        this.bias = rhs.bias;
    }
	
    /**
     * Constructor that takes size as a parameter, all other fields are left
     * default.
     * @param size the size of the layer.
     */
    public LayerConfiguration(int size) {
        this.size = size;
        this.activationFunction = new Sigmoid();
        this.bias = true;
    }

    /**
     * Constructor that takes size and activation function as a parameter, all
     * other fields are left default.
     * @param size the size of the layer.
     * @param activationFunction the activation function to use for the layer.
     */
    public LayerConfiguration(int size, ActivationFunction activationFunction) {
        this.size = size;
        this.activationFunction = activationFunction;
        this.bias = true;
    }

    /**
     * Constructor that takes size and bias as a parameter, all
     * other fields are left default.
     * @param size the size of the layer.
     * @param bias whether the layer has a bias.
     */
    public LayerConfiguration(int size, boolean bias) {
        this.size = size;
        this.bias = bias;
        this.activationFunction = new Sigmoid();
    }

    /**
     * Constructor that takes size and activation function and bias as a parameter,
     * all other fields are left default.
     * @param size the size of the layer.
     * @param activationFunction the activation function to use for the layer.
     * @param bias whether the layer has a bias.
     */
    public LayerConfiguration(int size, ActivationFunction activationFunction, boolean bias) {
        this.size = size;
        this.activationFunction = activationFunction;
        this.bias = bias;
    }

    /**
     * Gets the activation function.
     * @return the activation function.
     */
    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    /**
     * Sets the activation function.
     * @param activationFunction the new activation function.
     */
    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    /**
     * Gets the bias.
     * @return the bias.
     */
    public boolean isBias() {
        return bias;
    }

    /**
     * Sets the bias.
     * @param bias the new bias.
     */
    public void setBias(boolean bias) {
        this.bias = bias;
    }

    /**
     * Gets the size.
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size.
     * @param size the new size.
     */
    public void setSize(int size) {
        this.size = size;
    }
}
