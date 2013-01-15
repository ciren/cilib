/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class CascadeArchitectureBuilderTest {

    @Test
    public void testBuildBasicArchitecture() {
        Vector input = Vector.of(0.1, 0.2);
        StandardPattern pattern = new StandardPattern(input, input);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        //assert number of layers
        Assert.assertEquals(5, network.getArchitecture().getNumLayers());

        //assert number of weights
        Assert.assertEquals(24, network.getWeights().size());

        //assert layer sizes
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(0).size());
        Assert.assertEquals(1, network.getArchitecture().getLayers().get(1).size());
        Assert.assertEquals(1, network.getArchitecture().getLayers().get(2).size());
        Assert.assertEquals(1, network.getArchitecture().getLayers().get(3).size());
        Assert.assertEquals(2, network.getArchitecture().getLayers().get(4).size());

        //assert the number of weights for each neuron in the hidden layers
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).get(0).getNumWeights());
        Assert.assertEquals(4, network.getArchitecture().getLayers().get(2).get(0).getNumWeights());
        Assert.assertEquals(5, network.getArchitecture().getLayers().get(3).get(0).getNumWeights());

        //assert the number of weights for each output neuron
        Assert.assertEquals(6, network.getArchitecture().getLayers().get(4).get(0).getNumWeights());
        Assert.assertEquals(6, network.getArchitecture().getLayers().get(4).get(1).getNumWeights());
    }

    @Test
    public void testBuildComplexArchitecture() {
        Vector input = Vector.of(0.1, 0.2);
        StandardPattern pattern = new StandardPattern(input, input);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        //assert number of layers
        Assert.assertEquals(4, network.getArchitecture().getNumLayers());

        //assert number of weights
        Assert.assertEquals(29, network.getWeights().size());

        //assert layer sizes
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(0).size());
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).size());
        Assert.assertEquals(1, network.getArchitecture().getLayers().get(2).size());
        Assert.assertEquals(2, network.getArchitecture().getLayers().get(3).size());

        //assert the number of weights for each neuron in the first hidden layer
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).get(0).getNumWeights());
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).get(1).getNumWeights());
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).get(2).getNumWeights());

        //assert the number of weights for each neuron in the second hidden layer
        Assert.assertEquals(6, network.getArchitecture().getLayers().get(2).get(0).getNumWeights());

        //assert the number of weights for each output neuron
        Assert.assertEquals(7, network.getArchitecture().getLayers().get(3).get(0).getNumWeights());
        Assert.assertEquals(7, network.getArchitecture().getLayers().get(3).get(1).getNumWeights());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testEnforceMinimumLayerSize() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(0));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();
    }
}
