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
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.functions.activation.ActivationFunction;
import net.sourceforge.cilib.functions.activation.Sigmoid;

/**
 * Intermediate object that encapsulates the data that specifies a layer configuration.
 * This includes its size, the activation function to use and whether it has a
 * bias neuron.
 * @author andrich
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
     * @return the activation fuction.
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
