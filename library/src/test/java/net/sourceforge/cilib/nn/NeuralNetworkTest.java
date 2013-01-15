/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn;

import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.visitors.FeedForwardVisitor;
import net.sourceforge.cilib.nn.components.PatternInputSource;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class NeuralNetworkTest {

    private NeuralNetwork network;

    @Before
    public void setup() throws CIlibIOException {
        network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();
    }

    @Test
    public void testEvaluate() {
        network.setOperationVisitor(new FeedForwardVisitor());
        Vector input = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        StandardPattern pattern = new StandardPattern(input, input);

        FFNNTopology topology = new FFNNTopology(5, 3, 2, 0.0, 0.0);
        topology.setWeights(network.getWeights());
        network.setWeights(topology.getWeights());
        for (int i = 0; i < 2; i++) {
            Assert.assertEquals(((Real)topology.evaluate(new PatternInputSource(pattern)).get(i)).doubleValue(),
                    ((Real)network.evaluatePattern(pattern).get(i)).doubleValue(), Maths.EPSILON);
        }
    }
}
