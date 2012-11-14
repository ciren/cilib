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

import java.util.List;
import net.sourceforge.cilib.functions.activation.Linear;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;
import net.sourceforge.cilib.nn.components.Neuron;

/**
 *
 */
public class ElmanArchitectureBuilder extends ArchitectureBuilder {

    /**
     * Adds the layers to the architecture such that the architecture represents
     * an N layer Feed Forward Neural Network. All layers are fully connected and
     * hidden layers are constructed with a bias neuron if specified so by the
     * {@link LayerConfiguration}, the output layer does not have a bias neuron.
     * @param architecture {@inheritDoc }
     */
    @Override
    public void buildArchitecture(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        layers.clear();

        LayerBuilder layerBuilder = this.getLayerBuilder();
        List<LayerConfiguration> layerConfigurations = this.getLayerConfigurations();
        int listSize = layerConfigurations.size();

        layerConfigurations.get(listSize - 1).setBias(false); // output layer doesn't have bias

        // build the input layer
        ForwardingLayer inputLayer = new ForwardingLayer();
        inputLayer.setSourceSize(layerConfigurations.get(0).getSize());
        Neuron prototypeNeuron = new Neuron(); // create a simple prototype
        prototypeNeuron.setActivationFunction(new Linear()); // context nodes simply forward stuff
        for(int i = 0; i < layerConfigurations.get(1).getSize(); i++) { // add context nodes equal to the number of hidden units in the next layer
            inputLayer.add((Neuron)prototypeNeuron.getClone());
        }
        if (layerConfigurations.get(0).isBias()) { // add bias on top
            inputLayer.setBias(true);
            inputLayer.add(new BiasNeuron());
        }
        layers.add(inputLayer);

        Layer currentLayer = inputLayer;
        // build the rest of the layers - for now, there is only one context layer
        int previousLayerAbsoluteSize = currentLayer.size();
        //System.out.println("Size of input layer + context: " + previousLayerAbsoluteSize);
        for (int i = 1; i < listSize; i++) {
            currentLayer = layerBuilder.buildLayer(layerConfigurations.get(i), previousLayerAbsoluteSize);
            layers.add(currentLayer);
            previousLayerAbsoluteSize = currentLayer.size();
        }
    }
}
