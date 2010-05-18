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

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;

/**
 * Class represents an architecture building object, responsible for setting up
 * the layers correctly based on the specified layer configuration and the type
 * of architecture that is required. Different types of architectures are
 * constructed by extensions of this class. It depends on a {@link LayerBuilder}
 * to construct the layers themselves.
 * @author andrich
 */
public abstract class ArchitectureBuilder {

    private LayerBuilder layerBuilder;
    private List<LayerConfiguration> layerConfigurations;

    /**
     * Default constructor. The default LayerBuilder is a {@link PrototypeFullyConnectedLayerBuilder}.
     */
    public ArchitectureBuilder() {
        layerBuilder = new PrototypeFullyConnectedLayerBuilder();
        layerConfigurations = new ArrayList<LayerConfiguration>(3);
    }

    /**
     * Constructs the layers and adds them in the necessary order in to the given
     * architecture.
     * @param architecture the architecture to build.
     */
    public abstract void buildArchitecture(Architecture architecture);

    /**
     * Adds a layer configuration object to the stored layer configurations list.
     * @param layerConfiguration the layer configuration to add.
     */
    public void addLayer(LayerConfiguration layerConfiguration) {
        layerConfigurations.add(layerConfiguration);
    }

    /**
     * Gets the layer builder.
     * @return the layer builder.
     */
    public LayerBuilder getLayerBuilder() {
        return layerBuilder;
    }

    /**
     * Sets the layer builder.
     * @param layerBuilder the new layer builder.
     */
    public void setLayerBuilder(LayerBuilder layerBuilder) {
        this.layerBuilder = layerBuilder;
    }

    /**
     * Gets the layer configuration list.
     * @return the layer configuration list.
     */
    public List<LayerConfiguration> getLayerConfigurations() {
        return layerConfigurations;
    }

    /**
     * Sets the layer configuration list.
     * @param layerConfigurations the new layer configuration list.
     */
    public void setLayerConfigurations(List<LayerConfiguration> layerConfigurations) {
        this.layerConfigurations = layerConfigurations;
    }
}
