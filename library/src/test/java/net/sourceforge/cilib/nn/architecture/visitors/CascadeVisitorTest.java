/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.functions.activation.Linear;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class CascadeVisitorTest {

    @Test
    public void testBasicCascadeArchitecture() {
        Vector input = Vector.of(0.1, 0.2);
        StandardPattern pattern = new StandardPattern(input, input);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        Vector weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2,
                                   1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0,2.1,2.2,2.3,2.4);
        network.setWeights(weights);

        ArchitectureOperationVisitor cascadeVisitor = new CascadeVisitor();
        cascadeVisitor.setInput(pattern);
        cascadeVisitor.visit(network.getArchitecture());

        //hidden layer neurons
        double hn1 = 0.1*0.1 + 0.2*0.2 + 0.3*-1;
        double hn2 = 0.4*0.1 + 0.5*0.2 + 0.6*-1 + 0.7*hn1;
        double hn3 = 0.8*0.1 + 0.9*0.2 + 1.0*-1 + 1.1*hn1 + 1.2*hn2;

        //assert outputs
        Assert.assertEquals(1.3*0.1 + 1.4*0.2 + 1.5*-1 + 1.6*hn1 + 1.7*hn2 + 1.8*hn3,
                            cascadeVisitor.getOutput().get(0).doubleValue(),
                            Maths.EPSILON);
        Assert.assertEquals(1.9*0.1 + 2.0*0.2 + 2.1*-1 + 2.2*hn1 + 2.3*hn2 + 2.4*hn3,
                            cascadeVisitor.getOutput().get(1).doubleValue(),
                            Maths.EPSILON);
    }

    public void testComplexCascadeArchitecture() {
        Vector input = Vector.of(0.1, 0.2);
        StandardPattern pattern = new StandardPattern(input, input);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        Vector weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2,
                                   1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0,2.1,2.2,2.3,2.4);
        network.setWeights(weights);

        ArchitectureOperationVisitor cascadeVisitor = new CascadeVisitor();
        cascadeVisitor.setInput(pattern);
        cascadeVisitor.visit(network.getArchitecture());

        //hidden layer 1 neurons
        double hl1n1 = 0.1*0.1 + 0.2*0.2 + 0.3*-1;
        double hl1n2 = 0.4*0.1 + 0.5*0.2 + 0.6*-1;
        double hl1n3 = 0.7*0.1 + 0.8*0.2 + 0.9*-1;

        //hidden layer 2 neurons
        double hl2n1 = 1.1*0.1 + 1.2*0.2 + 1.3*-1 + 1.4*hl1n1 + 1.5*hl1n2 + 1.6*hl1n3;

        //assert outputs
        Assert.assertEquals(1.7*0.1 + 1.8*0.2 + 1.9*-1 + 2.0*hl1n1 + 2.1*hl1n2 + 2.2*hl1n3 + 2.3*hl2n1,
                            cascadeVisitor.getOutput().get(0).doubleValue(),
                            Maths.EPSILON);
        Assert.assertEquals(2.4*0.1 + 2.5*0.2 + 2.6*-1 + 2.7*hl1n1 + 2.8*hl1n2 + 2.9*hl1n3 + 3.0*hl2n1,
                            cascadeVisitor.getOutput().get(1).doubleValue(),
                            Maths.EPSILON);
    }
}
