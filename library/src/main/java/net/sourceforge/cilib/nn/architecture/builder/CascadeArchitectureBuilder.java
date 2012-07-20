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
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;

/**
 * <p>
 * Cascade network architecture consists of one input layer, one output layer and one hidden layer.
 * Each hidden neuron is fully connected with the entire input layer, as well as each hiddden neuron
 * added before it. The output layer is fully connected with the entire input layer and the entire
 * hidden layer.
 * </p>
 * <p>
 * Reference:
 * </p>
 * <p>
 * Scott E. Fahlman and Christian Lebiere, "The Cascade-Correlation Learning Architecture"
 * booktitle = "Advances in Neural Information Processing Systems 2"
 * pages = "524--532"
 * year = "1990"
 * publisher = "Morgan Kaufmann"
 * </p>
 */
public class CascadeArchitectureBuilder extends ArchitectureBuilder {

    /**
     * Adds the layers to the architecture such that the architecture represents
     * a Cascade network. All layers are fully connected to the input layer
     * Each hidden unit is connected to the hidden units added before it.
     * @param architecture {@inheritDoc }
     */
    @Override
    public void buildArchitecture(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        layers.clear();

        CascadeLayerBuilder cascadeLayerBuilder = new CascadeLayerBuilder();
		cascadeLayerBuilder.setDomain(this.getLayerBuilder().getDomain());
		
        List<LayerConfiguration> layerConfigurations = this.getLayerConfigurations();
        int listSize = layerConfigurations.size();

		if (listSize != 3) {
			throw new UnsupportedOperationException("The cascade network only support a three layer architecture.");
		}

        // build the input layer
        ForwardingLayer inputLayer = new ForwardingLayer();
        inputLayer.setSourceSize(layerConfigurations.get(0).getSize());
        if (layerConfigurations.get(0).isBias()) {
            inputLayer.setBias(true);
            inputLayer.add(new BiasNeuron());
        }
        layers.add(inputLayer);

        // build the hidden layer
        int sumOfPreviousLayerAbsoluteSizes = inputLayer.size();
        Layer currentLayer = cascadeLayerBuilder.buildLayer(layerConfigurations.get(1), sumOfPreviousLayerAbsoluteSizes);
        layers.add(currentLayer);
        sumOfPreviousLayerAbsoluteSizes += currentLayer.size();

		// build the output layer
		layerConfigurations.get(2).setBias(false);
        currentLayer = this.getLayerBuilder().buildLayer(layerConfigurations.get(2), sumOfPreviousLayerAbsoluteSizes);
        layers.add(currentLayer);
        sumOfPreviousLayerAbsoluteSizes += currentLayer.size();
    }
}
