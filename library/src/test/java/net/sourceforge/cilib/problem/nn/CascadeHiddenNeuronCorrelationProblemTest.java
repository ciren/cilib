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
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CascadeHiddenNeuronCorrelationProblemTest {

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

        CascadeHiddenNeuronCorrelationProblem problem = new CascadeHiddenNeuronCorrelationProblem();
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

        Vector error = problem.getErrorCache().get(0);
        assertEquals(0.635, error.getReal(0), Maths.EPSILON);
        assertEquals(1.015, error.getReal(1), Maths.EPSILON);

        error = problem.getErrorCache().get(1);
        assertEquals(0.46, error.getReal(0), Maths.EPSILON);
        assertEquals(0.7, error.getReal(1), Maths.EPSILON);

        Vector errorMeans = problem.getErrorMeans();
        assertEquals(0.5475, errorMeans.getReal(0), Maths.EPSILON);
        assertEquals(0.8575, errorMeans.getReal(1), Maths.EPSILON);

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

        error = problem.getErrorCache().get(0);
        assertEquals(1.777, error.getReal(0), Maths.EPSILON);
        assertEquals(2.5695, error.getReal(1), Maths.EPSILON);

        error = problem.getErrorCache().get(1);
        assertEquals(1.252, error.getReal(0), Maths.EPSILON);
        assertEquals(1.782, error.getReal(1), Maths.EPSILON);

        errorMeans = problem.getErrorMeans();
        assertEquals(1.5145, errorMeans.getReal(0), Maths.EPSILON);
        assertEquals(2.17575, errorMeans.getReal(1), Maths.EPSILON);

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
    public void testBasicCNCorrelation() {
        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1, new Linear()));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2, new Linear()));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();

        Vector weights = Vector.of(0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,
                                   1.1,1.2,1.3,1.4,1.5,1.6,1.7);
        network.setWeights(weights);

        StandardPatternDataTable trainingSet = new StandardPatternDataTable();
        Vector input = Vector.of(0.1, 0.2);
        Vector output = Vector.of(0, 0);
        StandardPattern pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        input = Vector.of(0.2, 0.4);
        pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);

        CascadeHiddenNeuronCorrelationProblem problem = new CascadeHiddenNeuronCorrelationProblem();
        problem.setTrainingSet(trainingSet);
        problem.setNeuralNetwork(network);
        Neuron neuron = new Neuron();
        neuron.setActivationFunction(new Linear());
        problem.setNeuron(neuron);
        problem.initialise();

        MaximisationFitness fitness = problem.calculateFitness(Vector.of(0.0, 0.0, 0.0, 0.0, 0.0));
        assertEquals(0.0, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(1.0, 0.0, 0.0, 0.0, 0.0));
        assertEquals(0.065625, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(0.0, 1.0, 0.0, 0.0, 0.0));
        assertEquals(0.13125, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(0.0, 0.0, 1.0, 0.0, 0.0));
        assertEquals(0.0, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(0.0, 0.0, 0.0, 1.0, 0.0));
        assertEquals(0.0328125, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(0.0, 0.0, 0.0, 0.0, 1.0));
        assertEquals(0.11484375, fitness.getValue(), Maths.EPSILON);

        fitness = problem.calculateFitness(Vector.of(1.0, 1.0, 1.0, 1.0, 1.0));
        assertEquals(0.34453125, fitness.getValue(), Maths.EPSILON);
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

        CascadeHiddenNeuronCorrelationProblem problem = new CascadeHiddenNeuronCorrelationProblem();
        problem.setTrainingSet(trainingSet);
        problem.setNeuralNetwork(network);
        Neuron neuron = new Neuron();
        neuron.setActivationFunction(new Linear());
        problem.setNeuron(neuron);
        problem.initialise();

        assertEquals("R(-3:3)^6", problem.getDomain().getDomainString());
    }
}
