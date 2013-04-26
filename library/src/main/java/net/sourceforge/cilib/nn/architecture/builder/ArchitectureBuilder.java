/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Class represents an architecture building object, responsible for setting up
 * the layers correctly based on the specified layer configuration and the type
 * of architecture that is required. Different types of architectures are
 * constructed by extensions of this class. It depends on a {@link LayerBuilder}
 * to construct the layers themselves.
 */
public abstract class ArchitectureBuilder implements Cloneable {

    private LayerBuilder layerBuilder;
    private List<LayerConfiguration> layerConfigurations;

    /**
     * Default constructor. The default LayerBuilder is a {@link PrototypeFullyConnectedLayerBuilder}.
     */
    public ArchitectureBuilder() {
        layerBuilder = new PrototypeFullyConnectedLayerBuilder();
        layerConfigurations = new ArrayList<LayerConfiguration>(3);
    }

    public ArchitectureBuilder(ArchitectureBuilder rhs) {
        layerBuilder = rhs.layerBuilder.getClone();

        layerConfigurations = new ArrayList<LayerConfiguration>();
        for (LayerConfiguration curLayer : rhs.layerConfigurations)
        layerConfigurations.add(new LayerConfiguration(curLayer));
    }

    public abstract ArchitectureBuilder getClone();

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
     * Adds a layer configuration object at the index in the stored layer configurations list.
	 * @param index the position at which the layer should be added.
     * @param layerConfiguration the layer configuration to add.
     */
    public void addLayer(int index, LayerConfiguration layerConfiguration) {
        layerConfigurations.add(index, layerConfiguration);
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
