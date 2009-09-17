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

import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;

/**
 *
 * @author andrich
 */
public class FeedForwardArchitectureBuilder extends ArchitectureBuilder {

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
        LayerBuilder layerBuilder = this.getLayerBuilder();
        List<LayerConfiguration> layerConfigurations = this.getLayerConfigurations();
        int listSize = layerConfigurations.size();

        layerConfigurations.get(listSize - 1).setBias(false); // output layer doesn't have bias

        // build the input layer
        ForwardingLayer inputLayer = new ForwardingLayer();
        inputLayer.setSourceSize(layerConfigurations.get(0).getSize());
        if (layerConfigurations.get(0).isBias()) {
            inputLayer.setBias(true);
            inputLayer.add(new BiasNeuron());
        }
        layers.add(inputLayer);

        Layer currentLayer = inputLayer;
        // build the rest of the layers
        int previousLayerAbsoluteSize = currentLayer.size();
        for (int i = 1; i < listSize; i++) {
            currentLayer = layerBuilder.buildLayer(layerConfigurations.get(i), previousLayerAbsoluteSize);
            layers.add(currentLayer);
            previousLayerAbsoluteSize = currentLayer.size();
        }
    }

}
