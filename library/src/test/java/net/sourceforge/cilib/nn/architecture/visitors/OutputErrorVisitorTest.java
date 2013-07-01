/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.architecture.visitors;

import net.cilib.io.pattern.StandardPattern;
import net.cilib.nn.NeuralNetwork;
import net.cilib.nn.architecture.builder.LayerConfiguration;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class OutputErrorVisitorTest {

    @Test
    public void testVisit() {
        Vector vector = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        Vector target = Vector.of(0.9, 0.9);
        StandardPattern pattern = new StandardPattern(vector, target);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, true));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        ArchitectureOperationVisitor visitor = new FeedForwardVisitor();
        visitor.setInput(pattern);
        visitor.visit(network.getArchitecture());
        Vector output = visitor.getOutput();

        visitor = new OutputErrorVisitor();
        visitor.setInput(pattern);
        visitor.visit(network.getArchitecture());
        Vector outputError = visitor.getOutput();

        Vector.Builder referenceError = Vector.newBuilder();
        for (int i = 0; i < output.size(); i++) {
            referenceError.add(Real.valueOf(target.doubleValueOf(i) - output.doubleValueOf(i)));
        }

        Assert.assertEquals(referenceError.build(), outputError);
    }
}
