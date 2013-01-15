/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This test does not compare against anything (the same problem as with unit testing
 * a PSO) but simply executes to see whether there is exceptions.
 */
public class NNSlidingWindowTrainingProblemTest {

    private NNSlidingWindowTrainingProblem problem;

    @Before
    public void setup() {
        problem = new NNSlidingWindowTrainingProblem();
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");
        problem.setTrainingSetPercentage(0.7);
        problem.setGeneralisationSetPercentage(0.3);
        problem.setStepSize(10);
        problem.setChangeFrequency(100);
        problem.setWindowSize(50);

        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        problem.initialise();
    }

    @Test
    public void testPercentages() {
        int refTraining = (int) (problem.getWindowSize() * 0.7);
        int refGeneralisation = (int) (problem.getWindowSize() * 0.3);
        Assert.assertEquals(refTraining, problem.getTrainingSet().size());
        Assert.assertEquals(refGeneralisation, problem.getGeneralisationSet().size());
    }

    @Test
    public void testCalculateFitness() {
        PSO pso = new PSO();
        pso.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1100));
        pso.setOptimisationProblem(problem);
        pso.performInitialisation();
        pso.performIteration();
    }
}
