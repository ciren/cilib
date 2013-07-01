/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.architecture.visitors;

import net.cilib.nn.NeuralNetwork;
import net.cilib.nn.architecture.Layer;
import net.cilib.nn.architecture.builder.LayerConfiguration;
import net.cilib.nn.components.Neuron;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class WeightSettingVisitorTest {

    @Test
    public void testVisit() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        Vector.Builder expectedWeightsBuilder = Vector.newBuilder();
        for (int i = 0; i < network.getWeights().size(); i++) {
            expectedWeightsBuilder.add(Real.valueOf(Math.random()));
        }
        Vector expectedWeights = expectedWeightsBuilder.build();

        WeightSettingVisitor visitor = new WeightSettingVisitor(expectedWeights);
        visitor.visit(network.getArchitecture());

        Vector.Builder actualWeightsBuilder = Vector.newBuilder();
        int numLayers = network.getArchitecture().getNumLayers();
        for (int i = 1; i < numLayers; i++) {
            Layer layer = network.getArchitecture().getLayers().get(i);
            for (Neuron neuron : layer) {
                actualWeightsBuilder.copyOf(neuron.getWeights());
            }
        }

        Assert.assertEquals(expectedWeights, actualWeightsBuilder.build());
    }
}
