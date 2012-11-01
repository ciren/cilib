/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.gd;

import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Before;
import org.junit.Test;

/**
 * This test does not compare against anything (the same problem as with unit
 * testing a PSO) but simply executes to see whether there is exceptions.
 */
public class GradientDecentBackpropagationTrainingTest {

    NNDataTrainingProblem problem;

    @Before
    public void setup() {
        problem = new NNDataTrainingProblem();
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");

        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
    }

    @Test
    public void testGradientDecent() {
        GradientDescentBackpropagationTraining training = new GradientDescentBackpropagationTraining();
        training.setOptimisationProblem(problem);
        StoppingCondition stoppingCondition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10);
        training.addStoppingCondition(stoppingCondition);
        training.performInitialisation();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < 10; i++) {
            training.algorithmIteration();
            builder.add(training.getBestSolution().getFitness().getValue());
        }
        
        Vector errors = builder.build();
        // asserts?
    }
}
