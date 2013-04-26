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
import net.sourceforge.cilib.cascadecorrelationalgorithm.CascadeCorrelationAlgorithm;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.measurement.single.CascadeNetworkWeightEvaluationCount;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.architecture.visitors.CascadeVisitor;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CascadeNetworkWeightEvaluationCountTest {

    @Test
    public void testPhase1() {
        CascadeNetworkWeightEvaluationCount measurement1 = new CascadeNetworkWeightEvaluationCount();
        measurement1.setOutputPenalty(1);
        CascadeNetworkWeightEvaluationCount measurement2 = new CascadeNetworkWeightEvaluationCount();
        measurement2.setCorrelationPenalty(1);
        CascadeNetworkWeightEvaluationCount measurement3 = new CascadeNetworkWeightEvaluationCount();
        measurement3.setCorrelationPenalty(1);
        measurement3.setCorrelationWeight(0.5);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.setOperationVisitor(new CascadeVisitor());
        network.initialise();

        StandardPatternDataTable trainingSet = new StandardPatternDataTable();
        Vector input = Vector.of(0.1, 0.2);
        Vector output = Vector.of(0, 0);
        StandardPattern pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        input = Vector.of(0.2, 0.4);
        pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        
        final NNTrainingProblem problem = mock(NNTrainingProblem.class);
        when(problem.getTrainingSet()).thenReturn(trainingSet);
        when(problem.getValidationSet()).thenReturn(new StandardPatternDataTable());
        when(problem.getGeneralisationSet()).thenReturn(new StandardPatternDataTable());
        when(problem.getNeuralNetwork()).thenReturn(network);

        PSO p1Alg = new PSO();
        p1Alg.addStoppingCondition(new MeasuredStoppingCondition());

        CascadeCorrelationAlgorithm cascadeAlg = new CascadeCorrelationAlgorithm();
        cascadeAlg.setOptimisationProblem(problem);
        cascadeAlg.setPhase1Algorithm(p1Alg);
        cascadeAlg.performInitialisation();

        assertEquals(0, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(0, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        
        cascadeAlg.phase1();

        assertEquals(60060, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(60060 + 20020, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals((60060 + 20020)/2.0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        
        cascadeAlg.phase1();

        assertEquals(60060 + 80080, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(60060 + 80080 + 20020 + 20020, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals((60060 + 80080 + 20020 + 20020)/2.0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
    }

    @Test
    public void testPhase2() {
        CascadeNetworkWeightEvaluationCount measurement1 = new CascadeNetworkWeightEvaluationCount();
        measurement1.setCorrelationPenalty(1);
        CascadeNetworkWeightEvaluationCount measurement2 = new CascadeNetworkWeightEvaluationCount();
        measurement2.setOutputPenalty(1);
        CascadeNetworkWeightEvaluationCount measurement3 = new CascadeNetworkWeightEvaluationCount();
        measurement3.setOutputPenalty(1);
        measurement3.setOutputWeight(0.5);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.setOperationVisitor(new CascadeVisitor());
        network.initialise();
        
        final NNTrainingProblem problem = mock(NNTrainingProblem.class);
        when(problem.getTrainingSet()).thenReturn(new StandardPatternDataTable());
        when(problem.getValidationSet()).thenReturn(new StandardPatternDataTable());
        when(problem.getGeneralisationSet()).thenReturn(new StandardPatternDataTable());
        when(problem.getNeuralNetwork()).thenReturn(network);

        PSO p2Alg = new PSO();
        p2Alg.addStoppingCondition(new MeasuredStoppingCondition());

        CascadeCorrelationAlgorithm cascadeAlg = new CascadeCorrelationAlgorithm();
        cascadeAlg.setOptimisationProblem(problem);
        cascadeAlg.setPhase2Algorithm(p2Alg);
        cascadeAlg.performInitialisation();
        
        assertEquals(0, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(0, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        
        cascadeAlg.phase2();

        assertEquals(120120, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(120120 + 40040, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals((120120 + 40040)/2.0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);

        network.getArchitecture().getArchitectureBuilder().addLayer(1, new LayerConfiguration(2));
        network.initialise();
        
        cascadeAlg.phase2();

        assertEquals(120120 + 200200, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(120120 + 200200 + 40040 + 40040, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals((120120 + 200200 + 40040 + 40040)/2.0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
    }

    @Test
    public void testAlgorithmIteration() {
        CascadeNetworkWeightEvaluationCount measurement1 = new CascadeNetworkWeightEvaluationCount();
        CascadeNetworkWeightEvaluationCount measurement2 = new CascadeNetworkWeightEvaluationCount();
        measurement2.setOutputPenalty(1);
        measurement2.setCorrelationPenalty(1);
        CascadeNetworkWeightEvaluationCount measurement3 = new CascadeNetworkWeightEvaluationCount();
        measurement3.setCorrelationPenalty(1);
        measurement3.setCorrelationWeight(0.5);
        measurement3.setOutputPenalty(1);
        measurement3.setOutputWeight(0.5);

        NeuralNetwork network = new NeuralNetwork();
        network.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network.setOperationVisitor(new CascadeVisitor());
        network.initialise();
        
        StandardPatternDataTable trainingSet = new StandardPatternDataTable();
        Vector input = Vector.of(0.1, 0.2);
        Vector output = Vector.of(0, 0);
        StandardPattern pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        input = Vector.of(0.2, 0.4);
        pattern = new StandardPattern(input, output);
        trainingSet.addRow(pattern);
        
        final NNTrainingProblem problem = mock(NNTrainingProblem.class);
        when(problem.getTrainingSet()).thenReturn(trainingSet);
        when(problem.getValidationSet()).thenReturn(new StandardPatternDataTable());
        when(problem.getGeneralisationSet()).thenReturn(new StandardPatternDataTable());
        when(problem.getNeuralNetwork()).thenReturn(network);

        PSO alg = new PSO();
        alg.addStoppingCondition(new MeasuredStoppingCondition());

        CascadeCorrelationAlgorithm cascadeAlg = new CascadeCorrelationAlgorithm();
        cascadeAlg.setOptimisationProblem(problem);
        cascadeAlg.setPhase1Algorithm(alg);
        cascadeAlg.setPhase2Algorithm(alg);
        cascadeAlg.performInitialisation();

        assertEquals(0, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(0, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        
        cascadeAlg.performIteration();

        assertEquals(120120, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(120120 + 40040, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals((120120 + 40040)/2.0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);

        cascadeAlg.performIteration();

        assertEquals(120120 + 60060 + 160160, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(120120 + 60060 + 160160 + 40040 + 20020 + 40040, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals((120120 + 60060 + 160160 + 40040 + 20020 + 40040)/2.0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        
        cascadeAlg.performIteration();

        assertEquals(120120 + 60060 + 160160 + 80080 + 200200, measurement1.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals(120120 + 60060 + 160160 + 80080 + 200200 + 40040 + 20020 + 40040 + 20020 + 40040, measurement2.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
        assertEquals((120120 + 60060 + 160160 + 80080 + 200200 + 40040 + 20020 + 40040 + 20020 + 40040)/2.0, measurement3.getValue(cascadeAlg).doubleValue(), Maths.EPSILON);
    }
}
