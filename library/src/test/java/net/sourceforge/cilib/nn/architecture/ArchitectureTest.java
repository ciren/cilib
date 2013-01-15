/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.visitors.WeightRetrievalVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArchitectureTest {

    private NeuralNetwork network;

    @Before
    public void setup() {
        network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();
    }

    @Test
    public void testInitialise() {
        // assert number of layers, it is not necessary to test constructed components
        // as they have their own unit tests.
        Assert.assertEquals(3, network.getArchitecture().getNumLayers());
    }

    @Test
    public void testAccept() {
        // this is mostly to test whether the visit actually occurs and not whether
        // the visitor works.
        WeightRetrievalVisitor visitor = new WeightRetrievalVisitor();
        network.getArchitecture().accept(visitor);
        Assert.assertEquals(22, visitor.getOutput().size());
    }
}
