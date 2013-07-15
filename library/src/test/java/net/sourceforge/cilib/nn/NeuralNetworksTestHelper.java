/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn;

import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.nn.architecture.builder.FeedForwardArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.builder.PrototypeFullyConnectedLayerBuilder;
import net.sourceforge.cilib.nn.domain.PresetNeuronDomain;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;

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
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        layerBuilder.setDomainProvider(domainProvider);
        architectureBuilder.setLayerBuilder(layerBuilder);
        architecture.setArchitectureBuilder(architectureBuilder);
        architecture.initialise();
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.setArchitecture(architecture);
        return neuralNetwork;
    }

    private NeuralNetworksTestHelper() { }
}
