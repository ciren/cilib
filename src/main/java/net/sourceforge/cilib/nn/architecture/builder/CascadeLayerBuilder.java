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
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.functions.activation.ActivationFunction;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class is a concrete extension of the abstract {@link LayerBuilder}. It constructs
 * a cascade layer by cloning a prototype neuron and adding to it weights such that it is
 * fully connected to the feeding layer, as well as all the neurons already in the current layer.
 */
public class CascadeLayerBuilder extends LayerBuilder {

    private Neuron prototypeNeuron;

    /**
     * Default constructor. Default neuron is a {@link Neuron}
     */
    public CascadeLayerBuilder() {
        prototypeNeuron = new Neuron();
    }

    /**
     * Builds a cascade layer by cloning a prototype neuron and adding to it weights such that it is
     * fully connected to the feeding layer, as well as all the neurons already in the current layer.
     * @param layerConfiguration
     * @param previousLayerAbsoluteSize Total sive of all previous layers.
     * @return the built layer.
     */
	@Override
    public Layer buildLayer(LayerConfiguration layerConfiguration, int sumOfPreviousLayerAbsoluteSizes) {
        prototypeNeuron.setActivationFunction((ActivationFunction)layerConfiguration.getActivationFunction());
        int layerSize = layerConfiguration.getSize();

        Layer layer = new Layer();
        for (int i = 0; i < layerSize; i++) {
            Neuron newNeuron = (Neuron) prototypeNeuron.getClone();
            Real domainReal = null;
            try {
                domainReal = (Real)((Vector)this.getDomainRegistry().getBuiltRepresenation()).get(0);
            }
            catch(ClassCastException exception) {
                throw new UnsupportedOperationException("The domain string of the neural network weights has to be real valued");
            }

            Real weight = Real.valueOf(domainReal.doubleValue(), domainReal.getBounds());
            Vector weights = newNeuron.getWeights();
            weights.add(weight);
			
            for (int j = 1; j < sumOfPreviousLayerAbsoluteSizes + i; j++) {
                Real newWeight = weight.getClone();
                weights.add(newWeight);
            }

            this.getWeightInitializationStrategy().initialize(weights);
            layer.add(newNeuron);
        }
        return layer;
    }

    /**
     * Get the prototype neuron.
     * @return the prototype neuron.
     */
    public Neuron getPrototypeNeuron() {
        return prototypeNeuron;
    }

    /**
     * Set the prototype neuron.
     * @param prototypeNeuron the prototype neuron.
     */
    public void setPrototypeNeuron(Neuron prototypeNeuron) {
        this.prototypeNeuron = prototypeNeuron;
    }
}
