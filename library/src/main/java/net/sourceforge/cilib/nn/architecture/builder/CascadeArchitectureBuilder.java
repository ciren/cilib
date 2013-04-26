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
 * <p>
 * Cascade network architecture consists of one input layer, one output layer and multiple hidden layers.
 * Each hidden layer is fully connected with the entire input layer, as well as each hidden layer
 * added before it. The output layer is fully connected with the entire input layer and the each
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

    public CascadeArchitectureBuilder() {}

    public CascadeArchitectureBuilder(CascadeArchitectureBuilder rhs) {
        super(rhs);
    }

    @Override
    public CascadeArchitectureBuilder getClone() {
        return new CascadeArchitectureBuilder(this);
    }

    /**
     * Adds the layers to the architecture such that the architecture represents
     * a Cascade network. All layers are fully connected to the input layer
     * Each hidden layer is fully connected to the hidden layer added before it.
     * @param architecture {@inheritDoc }
     */
    @Override
    public void buildArchitecture(Architecture architecture) {
        List<Layer> layers = architecture.getLayers();
        layers.clear();

        List<LayerConfiguration> layerConfigurations = this.getLayerConfigurations();
        int listSize = layerConfigurations.size();

        // build the input layer
        ForwardingLayer inputLayer = new ForwardingLayer();
        inputLayer.setSourceSize(layerConfigurations.get(0).getSize());
        if (layerConfigurations.get(0).isBias()) {
            inputLayer.setBias(true);
            inputLayer.add(new BiasNeuron());
        }
        layers.add(inputLayer);

        // build the hidden layers and output layer
        int sumOfPreviousLayerAbsoluteSizes = inputLayer.size();
        for (int curLayer = 1; curLayer < layerConfigurations.size(); ++curLayer) {
            if (layerConfigurations.get(curLayer).getSize() == 0)
                throw new UnsupportedOperationException("Hidden layers must have at least one neuron each.");

            layerConfigurations.get(curLayer).setBias(false); //All neurons have access to the bias in the input layer.
            Layer newLayer = this.getLayerBuilder().buildLayer(layerConfigurations.get(curLayer), sumOfPreviousLayerAbsoluteSizes);
            layers.add(newLayer);
            sumOfPreviousLayerAbsoluteSizes += newLayer.size();
        }
    }
}
