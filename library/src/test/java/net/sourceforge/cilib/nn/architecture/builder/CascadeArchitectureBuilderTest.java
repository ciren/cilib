/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.nn.domain.FaninNeuronDomain;
import net.sourceforge.cilib.nn.domain.PresetNeuronDomain;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
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
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
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
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
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

    @Test
    public void testPresetDomain() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(12));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network.initialise();

        Assert.assertEquals("R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^16,R(-3:3)^16",
                            network.getArchitecture().getDomain().getDomainString());
        Assert.assertEquals(80, ((Vector) network.getArchitecture().getDomain().getBuiltRepresentation()).size());
    }  

    @Test
    public void testFaninDomain() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(12));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(new FaninNeuronDomain());
        network.initialise();

        Assert.assertEquals("R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.25:0.25)^16,R(-0.25:0.25)^16",
                            network.getArchitecture().getDomain().getDomainString());
        Assert.assertEquals(80, ((Vector) network.getArchitecture().getDomain().getBuiltRepresentation()).size());
    }  

    @Test (expected = UnsupportedOperationException.class)
    public void testEnforceMinimumLayerSize() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(0));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network.initialise();
    }
}
