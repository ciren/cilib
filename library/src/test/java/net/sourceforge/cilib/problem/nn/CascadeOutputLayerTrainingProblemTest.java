/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.functions.activation.Linear;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CascadeOutputLayerTrainingProblemTest {

    @Test
    public void testBasicCNCacheGeneration() {
        StandardPatternDataTable trainingSet = new StandardPatternDataTable();
        Vector input = Vector.of(0.1, 0.2);
        Vector output = Vector.of(0, 0);
        StandardPattern pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        input = Vector.of(0.2, 0.4);
        pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);

        CascadeOutputLayerTrainingProblem problem = new CascadeOutputLayerTrainingProblem();
        problem.setTrainingSet(trainingSet);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();
        
        Vector weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,
                                   1.1);
        network.setWeights(weights);
        
        problem.setNeuralNetwork(network);
        problem.initialise();

        Layer layer = problem.getActivationCache().get(0);
        assertEquals(6, layer.size());
        assertEquals(0.1, layer.getNeuron(0).getActivation(), Maths.EPSILON);
        assertEquals(0.2, layer.getNeuron(1).getActivation(), Maths.EPSILON);
        assertEquals(-1.0, layer.getNeuron(2).getActivation(), Maths.EPSILON);
        assertEquals(-0.25, layer.getNeuron(3).getActivation(), Maths.EPSILON);
        assertEquals(-0.635, layer.getNeuron(4).getActivation(), Maths.EPSILON);
        assertEquals(-1.015, layer.getNeuron(5).getActivation(), Maths.EPSILON);

        layer = problem.getActivationCache().get(1);
        assertEquals(6, layer.size());
        assertEquals(0.2, layer.getNeuron(0).getActivation(), Maths.EPSILON);
        assertEquals(0.4, layer.getNeuron(1).getActivation(), Maths.EPSILON);
        assertEquals(-1.0, layer.getNeuron(2).getActivation(), Maths.EPSILON);
        assertEquals(-0.2, layer.getNeuron(3).getActivation(), Maths.EPSILON);
        assertEquals(-0.46, layer.getNeuron(4).getActivation(), Maths.EPSILON);
        assertEquals(-0.7, layer.getNeuron(5).getActivation(), Maths.EPSILON);
        
        network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,
                                   1.1,1.2,1.3,1.4,1.5,1.6,1.7);
        network.setWeights(weights);
        
        problem.setNeuralNetwork(network);
        problem.initialise();

        layer = problem.getActivationCache().get(0);
        assertEquals(7, layer.size());
        assertEquals(0.1, layer.getNeuron(0).getActivation(), Maths.EPSILON);
        assertEquals(0.2, layer.getNeuron(1).getActivation(), Maths.EPSILON);
        assertEquals(-1.0, layer.getNeuron(2).getActivation(), Maths.EPSILON);
        assertEquals(-0.25, layer.getNeuron(3).getActivation(), Maths.EPSILON);
        assertEquals(-0.635, layer.getNeuron(4).getActivation(), Maths.EPSILON);
        assertEquals(-1.777, layer.getNeuron(5).getActivation(), Maths.EPSILON);
        assertEquals(-2.5695, layer.getNeuron(6).getActivation(), Maths.EPSILON);

        layer = problem.getActivationCache().get(1);
        assertEquals(7, layer.size());
        assertEquals(0.2, layer.getNeuron(0).getActivation(), Maths.EPSILON);
        assertEquals(0.4, layer.getNeuron(1).getActivation(), Maths.EPSILON);
        assertEquals(-1.0, layer.getNeuron(2).getActivation(), Maths.EPSILON);
        assertEquals(-0.2, layer.getNeuron(3).getActivation(), Maths.EPSILON);
        assertEquals(-0.46, layer.getNeuron(4).getActivation(), Maths.EPSILON);
        assertEquals(-1.252, layer.getNeuron(5).getActivation(), Maths.EPSILON);
        assertEquals(-1.782, layer.getNeuron(6).getActivation(), Maths.EPSILON);
    }

    @Test
    public void testBasicCNOutput() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        Vector weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.0,0.0,0.0,
                                   0.0,0.0,0.0,0.0,0.0,0.0,0.0);
        network.setWeights(weights);

        StandardPatternDataTable trainingSet = new StandardPatternDataTable();
        Vector input = Vector.of(0.1, 0.2);
        Vector output = Vector.of(0, 0);
        StandardPattern pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        input = Vector.of(0.2, 0.4);
        pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);

        CascadeOutputLayerTrainingProblem problem = new CascadeOutputLayerTrainingProblem();
        problem.setTrainingSet(trainingSet);
        problem.setNeuralNetwork(network);
        problem.initialise();

        MinimisationFitness fitness = problem.calculateFitness(Vector.of(0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7));
        assertEquals(3.625771812499999, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        assertEquals(0.0, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0));
        assertEquals(1.8179125, fitness.getValue(), Maths.EPSILON);
    }

    @Test
    public void testDomain() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();
        
        Vector weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,
                                   1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0,
                                   2.1);
        network.setWeights(weights);

        StandardPatternDataTable trainingSet = new StandardPatternDataTable();
        Vector input = Vector.of(0.1, 0.2);
        Vector output = Vector.of(0, 0);
        StandardPattern pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        input = Vector.of(0.2, 0.4);
        pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);

        CascadeOutputLayerTrainingProblem problem = new CascadeOutputLayerTrainingProblem();
        problem.setTrainingSet(trainingSet);
        problem.setNeuralNetwork(network);
        problem.initialise();

        assertEquals("R(-3:3)^12", problem.getDomain().getDomainString());
    }
}
