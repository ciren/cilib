package net.sourceforge.cilib.nn;

import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.builder.FeedForwardArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.builder.PrototypeFullyConnectedLayerBuilder;

/**
 * Helper object for neural network testing.
 */
public final class NeuralNetworksTestHelper {

    public static NeuralNetwork createFFNN(int input, int hidden, int output) {
        Architecture architecture = new Architecture();
        FeedForwardArchitectureBuilder architectureBuilder = new FeedForwardArchitectureBuilder();
        architectureBuilder.addLayer(new LayerConfiguration(input));
        architectureBuilder.addLayer(new LayerConfiguration(hidden));
        architectureBuilder.addLayer(new LayerConfiguration(output));
        final PrototypeFullyConnectedLayerBuilder layerBuilder = new PrototypeFullyConnectedLayerBuilder();
        layerBuilder.setDomain("R(-3:3)");
        architectureBuilder.setLayerBuilder(layerBuilder);
        architecture.setArchitectureBuilder(architectureBuilder);
        architecture.initialize();
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.setArchitecture(architecture);
        return neuralNetwork;
    }

    private NeuralNetworksTestHelper() { }
}
