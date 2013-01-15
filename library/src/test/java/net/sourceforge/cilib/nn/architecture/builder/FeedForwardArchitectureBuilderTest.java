/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.NeuralNetwork;
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
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
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
}
