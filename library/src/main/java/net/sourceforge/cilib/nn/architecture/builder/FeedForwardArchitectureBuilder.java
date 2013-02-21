/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import java.util.List;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.ForwardingLayer;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.BiasNeuron;

/**
 *
 */
public class FeedForwardArchitectureBuilder extends ArchitectureBuilder {

    public FeedForwardArchitectureBuilder() {}

    public FeedForwardArchitectureBuilder(FeedForwardArchitectureBuilder rhs) {
        super(rhs);
    }

    @Override
    public FeedForwardArchitectureBuilder getClone() {
        return new FeedForwardArchitectureBuilder(this);
    }

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
