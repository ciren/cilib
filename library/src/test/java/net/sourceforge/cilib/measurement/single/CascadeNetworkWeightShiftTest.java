/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.visitors.CascadeVisitor;
import net.sourceforge.cilib.nn.domain.PresetNeuronDomain;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CascadeNetworkWeightShiftTest {

    @Test
    public void testMeasurement() {
        CascadeNetworkWeightShift measurement = new CascadeNetworkWeightShift();

        NeuralNetwork network1 = new NeuralNetwork();
        network1.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        StringBasedDomainRegistry domain = new StringBasedDomainRegistry();
        domain.setDomainString("R(-3:3)");
        PresetNeuronDomain domainProvider = new PresetNeuronDomain();
        domainProvider.setWeightDomainPrototype(domain);
        network1.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network1.setOperationVisitor(new CascadeVisitor());
        network1.initialise();
        
        NeuralNetwork network2 = new NeuralNetwork();
        network2.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network2.setOperationVisitor(new CascadeVisitor());
        network2.initialise();
        
        NeuralNetwork network3 = new NeuralNetwork();
        network3.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network3.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network3.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network3.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network3.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network3.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network3.setOperationVisitor(new CascadeVisitor());
        network3.initialise();
        
        NeuralNetwork network4 = new NeuralNetwork();
        network4.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network4.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network4.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network4.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network4.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        network4.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network4.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomainProvider(domainProvider);
        network4.setOperationVisitor(new CascadeVisitor());
        network4.initialise();

        NNTrainingProblem problem = mock(NNTrainingProblem.class);
        when(problem.getNeuralNetwork()).thenReturn(network1);

        Algorithm algorithm = mock(Algorithm.class);
        when(algorithm.getOptimisationProblem()).thenReturn(problem);

        Vector solution = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5, 0.6);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(1.1, 1.2, 1.3, 1.4, 1.5, 1.6);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        
        when(problem.getNeuralNetwork()).thenReturn(network2);
        solution = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0,
                             1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0,
                             2.1);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                             0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                             0.0);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, Double.NaN,
                             Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
                             Double.NaN);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        
        when(problem.getNeuralNetwork()).thenReturn(network3);
        solution = Vector.of(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0,
                             1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0,
                             2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                             0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                             0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(900, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,
                             10.0, 10.0, 10.0, 10.0, 10.0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
                             Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        
        when(problem.getNeuralNetwork()).thenReturn(network4);
        solution = Vector.of(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,
                             10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,
                             10.0, 10.0, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 3.0,
                             3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                             0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                             0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                             0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(1500, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
        solution = Vector.of(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,
                             10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,
                             10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,
                             10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0);
        when(algorithm.getBestSolution()).thenReturn(new OptimisationSolution(solution, InferiorFitness.instance()));
        assertEquals(0, measurement.getValue(algorithm).doubleValue(), Maths.EPSILON);
    }
}
