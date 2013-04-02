/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.cascadecorrelationalgorithm;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.activation.Linear;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.Layer;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CascadeCorrelationAlgorithmTest {

    @Test
    public void testPhase1() {

        NNDataTrainingProblem problem = new NNDataTrainingProblem();

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.initialise();
        
        network.setWeights(Vector.of(0.0,0.0,0.0,0.0,0.0,0.0));
        problem.setNeuralNetwork(network);

        ArrayList<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        solutions.add(new OptimisationSolution(Vector.of(1.0, 1.0, 1.0), new MaximisationFitness(0.0)));
        solutions.add(new OptimisationSolution(Vector.of(2.0, 2.0, 2.0), new MaximisationFitness(0.0)));
        
        final AbstractAlgorithm p1Alg = mock(AbstractAlgorithm.class);
        final AbstractAlgorithm p1AlgClone = mock(AbstractAlgorithm.class);
        when(p1Alg.getClone()).thenReturn(p1AlgClone);
        when(p1AlgClone.getSolutions()).thenReturn(solutions);
        doNothing().when(p1AlgClone).performInitialisation();
        doNothing().when(p1AlgClone).run();

        CascadeCorrelationAlgorithm cascadeAlg = new CascadeCorrelationAlgorithm();
        cascadeAlg.setOptimisationProblem(problem);
        cascadeAlg.setPhase1Algorithm(p1Alg);
        cascadeAlg.phase1();

        List<Layer> resultLayers = network.getArchitecture().getLayers();
        assertEquals(3, resultLayers.size());
        assertEquals(3, resultLayers.get(0).size());
        assertEquals(2, resultLayers.get(1).size());
        assertEquals(2, resultLayers.get(2).size());

        Vector resultWeights = network.getWeights();
        assertEquals(1.0, resultWeights.doubleValueOf(0), Maths.EPSILON);
        assertEquals(1.0, resultWeights.doubleValueOf(1), Maths.EPSILON);
        assertEquals(1.0, resultWeights.doubleValueOf(2), Maths.EPSILON);
        assertEquals(2.0, resultWeights.doubleValueOf(3), Maths.EPSILON);
        assertEquals(2.0, resultWeights.doubleValueOf(4), Maths.EPSILON);
        assertEquals(2.0, resultWeights.doubleValueOf(5), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(6), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(7), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(8), Maths.EPSILON);
        assertTrue(Double.isNaN(resultWeights.doubleValueOf(9)));
        assertTrue(Double.isNaN(resultWeights.doubleValueOf(10)));
        assertEquals(0.0, resultWeights.doubleValueOf(11), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(12), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(13), Maths.EPSILON);
        assertTrue(Double.isNaN(resultWeights.doubleValueOf(14)));
        assertTrue(Double.isNaN(resultWeights.doubleValueOf(15)));
        
        solutions.clear();
        solutions.add(new OptimisationSolution(Vector.of(1.0, 1.0, 1.0, 1.0, 1.0), new MaximisationFitness(0.0)));
        network.setWeights(Vector.of(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
                                     0.0,0.0,0.0,0.0,0.0,0.0));
        cascadeAlg.phase1();

        resultLayers = network.getArchitecture().getLayers();
        assertEquals(4, resultLayers.size());
        assertEquals(3, resultLayers.get(0).size());
        assertEquals(2, resultLayers.get(1).size());
        assertEquals(1, resultLayers.get(2).size());
        assertEquals(2, resultLayers.get(3).size());

        resultWeights = network.getWeights();
        assertEquals(0.0, resultWeights.doubleValueOf(0), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(1), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(2), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(3), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(4), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(5), Maths.EPSILON);
        assertEquals(1.0, resultWeights.doubleValueOf(6), Maths.EPSILON);
        assertEquals(1.0, resultWeights.doubleValueOf(7), Maths.EPSILON);
        assertEquals(1.0, resultWeights.doubleValueOf(8), Maths.EPSILON);
        assertEquals(1.0, resultWeights.doubleValueOf(9), Maths.EPSILON);
        assertEquals(1.0, resultWeights.doubleValueOf(10), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(11), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(12), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(13), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(14), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(15), Maths.EPSILON);
        assertTrue(Double.isNaN(resultWeights.doubleValueOf(16)));
        assertEquals(0.0, resultWeights.doubleValueOf(17), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(18), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(19), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(20), Maths.EPSILON);
        assertEquals(0.0, resultWeights.doubleValueOf(21), Maths.EPSILON);
        assertTrue(Double.isNaN(resultWeights.doubleValueOf(22)));
    }
}
