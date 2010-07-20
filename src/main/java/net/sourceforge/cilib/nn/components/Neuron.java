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

import net.sourceforge.cilib.functions.activation.ActivationFunction;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.nn.architecture.NeuralInputSource;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Class represents a neuron within a neural network and encapsulates weights
 * (that lead in to the neuron), an activation function and also stores its
 * current activation.
 * @author andrich
 */
public class Neuron implements Cloneable {

    private double activation; // the stored activation after the last calculateActivation call.
    private ActivationFunction activationFunction; // the neuron's activation function.
    private Vector weights; // the set of input weights for the neuron.

    /**
     * Default constructor.
     * Activation is set to 0.0. The default Activation Function is {@link Sigmoid}
     * and the weights are initialized as an empty {@link Vector}.
     */
    public Neuron() {
        activation = 0.0;
        activationFunction = new Sigmoid();
        weights = new Vector();
    }

    /**
     * Copy constructor
     * @param neuron the neuron to copy.
     */
    public Neuron(Neuron neuron) {
        activation = neuron.getActivation();
        activationFunction = (ActivationFunction) neuron.activationFunction;
        weights = Vector.copyOf(neuron.weights);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Object getClone() {
        return new Neuron(this);
    }

    /**
     * Calculates the neuron's activation by taking the dot product of the
     * netInputSource and the weights Vector and passing the result to the
     * activation function.
     * @param netInputSource the neuron's input source.
     * @return the neuron's activation.
     */
    public double calculateActivation(NeuralInputSource netInputSource) {
        double netInput = 0.0;

        int size = this.weights.size();
        for (int i = 0; i < size; i++) {
            netInput += netInputSource.getNeuralInput(i) * this.weights.doubleValueOf(i);
        }
        this.activation = activationFunction.apply(netInput);
        return activation;
    }

    /**
     * Gets the number of weights leading in to the neuron.
     * @return the number of input weights.
     */
    public int getNumWeights() {
        return this.weights.size();
    }

    /**
     * Gets the stored activation.
     * @return the stored activation after the last call of {@link #calculateActivation(net.sourceforge.cilib.nn.architecture.NeuralInputSource). }
     */
    public double getActivation() {
        return activation;
    }

    /**
     * Sets the neuron's activation.
     * @param activation the neuron's new activation.
     */
    public void setActivation(double activation) {
        this.activation = activation;
    }

    /**
     * Gets the neuron's activation function.
     * @return the neuron's activation function.
     */
    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    /**
     * Sets the neuron's activation function.
     * @param activationFunction the neuron's activation function.
     */
    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    /**
     * Gets the neuron's weights.
     * @return the neuron's weights.
     */
    public Vector getWeights() {
        return weights;
    }

    /**
     * Sets the neuron's weights.
     * @param weights the neuron's new weights.
     */
    public void setWeights(Vector weights) {
        this.weights = weights;
    }
}
