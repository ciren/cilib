/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.domain.FaninNeuronDomain;
import net.sourceforge.cilib.nn.domain.PresetNeuronDomain;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import org.junit.Assert;
import org.junit.Test;

public class FeedForwardArchitectureBuilderTest {

    @Test
    public void testBuildArchitecture() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, false));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network.initialise();

        //assert num layers
        Assert.assertEquals(4, network.getArchitecture().getNumLayers());

        //assert fully connected
        Assert.assertEquals(36, network.getWeights().size());

        //assert biasses
        Assert.assertEquals(true, network.getArchitecture().getLayers().get(0).isBias());
        int layerSize = network.getArchitecture().getLayers().get(0).size();
        Assert.assertEquals(6, layerSize);
        Assert.assertEquals(-1, network.getArchitecture().getLayers().get(0).getNeuralInput(layerSize - 1), Maths.EPSILON);

        Assert.assertEquals(true, network.getArchitecture().getLayers().get(1).isBias());
        layerSize = network.getArchitecture().getLayers().get(1).size();
        Assert.assertEquals(4, layerSize);
        Assert.assertEquals(-1, network.getArchitecture().getLayers().get(1).getNeuralInput(layerSize - 1), Maths.EPSILON);

        Assert.assertEquals(false, network.getArchitecture().getLayers().get(2).isBias());
        layerSize = network.getArchitecture().getLayers().get(2).size();
        Assert.assertEquals(3, layerSize);

        Assert.assertEquals(false, network.getArchitecture().getLayers().get(3).isBias());
        layerSize = network.getArchitecture().getLayers().get(3).size();
        Assert.assertEquals(2, layerSize);
    }

    @Test
    public void testPresetDomain() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, false));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network.initialise();
        
        Assert.assertEquals("R(-3:3)^6,R(-3:3)^6,R(-3:3)^6,R(-3:3)^4,R(-3:3)^4,R(-3:3)^4,R(-3:3)^3,R(-3:3)^3",
                            network.getArchitecture().getDomain().getDomainString());
        Assert.assertEquals(36, ((Vector) network.getArchitecture().getDomain().getBuiltRepresentation()).size());
    }

    @Test
    public void testFaninDomain() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4, false));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(new FaninNeuronDomain());
        network.initialise();
        
        Assert.assertEquals("R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4,R(-0.5:0.5)^4",
                            network.getArchitecture().getDomain().getDomainString());
        Assert.assertEquals(36, ((Vector) network.getArchitecture().getDomain().getBuiltRepresentation()).size());
    }  
}
