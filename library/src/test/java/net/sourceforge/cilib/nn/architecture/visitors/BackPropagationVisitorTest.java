/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.architecture.visitors;

import net.cilib.io.pattern.StandardPattern;
import net.cilib.math.Maths;
import net.cilib.nn.FFNNTopology;
import net.cilib.nn.NeuralNetwork;
import net.cilib.nn.architecture.builder.LayerConfiguration;
import net.cilib.nn.components.PatternInputSource;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class BackPropagationVisitorTest {

    @Test
    public void testVisit3Layers() {
        Vector input = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        Vector target = Vector.of(0.1, 0.9);
        StandardPattern pattern = new StandardPattern(input, target);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        FFNNTopology topology = new FFNNTopology(5, 3, 2, 0.1, 0.9);

        topology.setWeights(network.getWeights());

        BackPropagationVisitor visitor = new BackPropagationVisitor();
        double[][] previousWeightUpdates = null;

        for (int i = 0; i < 3; i++) {
            //FF
            topology.evaluate(new PatternInputSource(pattern));
            network.evaluatePattern(pattern);

            //BP
            topology.train();

            visitor.setPreviousPattern(pattern);
            visitor.setPreviousWeightUpdates(previousWeightUpdates);
            visitor.visit(network.getArchitecture());
            previousWeightUpdates = visitor.getPreviousWeightUpdates();

            Vector topologyWeights = topology.getWeights();
            Vector networkWeights = network.getWeights();
            for (int j = 0; j < topology.getWeights().size(); j++) {
                Assert.assertEquals(topologyWeights.doubleValueOf(j), networkWeights.doubleValueOf(j), Maths.EPSILON);
            }
        }

    }

    /*
     * This does not actually assert anything (I have nothing to compare
     * against) it just runs and if an exception occurs, the test will fail.
     */
    @Test
    public void testVisit5Layers() {
        Vector input = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        Real target = Real.valueOf(0.9);
        StandardPattern pattern = new StandardPattern(input, target);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        BackPropagationVisitor visitor = new BackPropagationVisitor();
        double[][] previousWeightUpdates = null;

        for (int i = 0; i < 3; i++) {

            //FF
            network.evaluatePattern(pattern);

            //BP
            visitor.setPreviousPattern(pattern);
            visitor.setPreviousWeightUpdates(previousWeightUpdates);
            visitor.visit(network.getArchitecture());
            previousWeightUpdates = visitor.getPreviousWeightUpdates();
        }
    }
}
