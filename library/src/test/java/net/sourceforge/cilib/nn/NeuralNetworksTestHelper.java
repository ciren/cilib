/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn;

import net.cilib.nn.architecture.Architecture;
import net.cilib.nn.architecture.builder.FeedForwardArchitectureBuilder;
import net.cilib.nn.architecture.builder.LayerConfiguration;
import net.cilib.nn.architecture.builder.PrototypeFullyConnectedLayerBuilder;

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
        architecture.initialise();
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.setArchitecture(architecture);
        return neuralNetwork;
    }

    private NeuralNetworksTestHelper() { }
}
