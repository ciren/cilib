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
    public void testBuildArchitecture() {
		Vector input = Vector.of(0.1, 0.2);
        StandardPattern pattern = new StandardPattern(input, input);
		
        NeuralNetwork network = new NeuralNetwork();
		network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();

        //assert number of layers
        Assert.assertEquals(3, network.getArchitecture().getNumLayers());

        //assert number of weights
        Assert.assertEquals(24, network.getWeights().size());

        //assert layer sizes
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(0).size());
        Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).size());
        Assert.assertEquals(2, network.getArchitecture().getLayers().get(2).size());

		//assert the number of weights for each hidden neuron
		Assert.assertEquals(3, network.getArchitecture().getLayers().get(1).get(0).getNumWeights());
		Assert.assertEquals(4, network.getArchitecture().getLayers().get(1).get(1).getNumWeights());
		Assert.assertEquals(5, network.getArchitecture().getLayers().get(1).get(2).getNumWeights());

		//assert the number of weights for each output neuron
		Assert.assertEquals(6, network.getArchitecture().getLayers().get(2).get(0).getNumWeights());
		Assert.assertEquals(6, network.getArchitecture().getLayers().get(2).get(1).getNumWeights());
    }

	@Test (expected = UnsupportedOperationException.class)
	public void testEnforceMinimumNumberOfLayers() {
		NeuralNetwork network = new NeuralNetwork();
		network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();
	}

	@Test (expected = UnsupportedOperationException.class)
	public void testEnforceMaximumNumberOfLayers() {
		NeuralNetwork network = new NeuralNetwork();
		network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialize();
	}
}
