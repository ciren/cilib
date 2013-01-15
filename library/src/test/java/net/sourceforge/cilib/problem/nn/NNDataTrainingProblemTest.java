/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.nn;

import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.DomainRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test does not compare against anything (the same problem as with unit testing
 * a PSO) but simply executes to see whether there is exceptions.
 */
public class NNDataTrainingProblemTest {

    NNDataTrainingProblem problem;

    @Before
    public void setup() {
        problem = new NNDataTrainingProblem();
        problem.getDataTableBuilder().setDataReader(new ARFFFileReader());
        problem.getDataTableBuilder().setSourceURL("library/src/test/resources/datasets/iris.arff");
        problem.setTrainingSetPercentage(0.5);
        problem.setValidationSetPercentage(0.2);
        problem.setGeneralisationSetPercentage(0.3);

        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(4));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(1));
        problem.getNeuralNetwork().getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        problem.initialise();
    }

    @Test
    public void testPercentages() {
        int refTraining = (int) (150 * 0.5); // 150 pattern in iris
        int refValidation = (int) (150 * 0.2);
        int refGeneralisation = (int) (150 * 0.3); // 150 pattern in iris
        assertEquals(refTraining, problem.getTrainingSet().size());
        assertEquals(refGeneralisation, problem.getGeneralisationSet().size());
    }

    @Test
    public void testCalculateFitness() {
        PSO pso = new PSO();
        pso.addStoppingCondition(new MeasuredStoppingCondition());
        pso.setOptimisationProblem(problem);
        pso.performInitialisation();
        pso.performIteration();
    }

    @Test
    public void shouldInitialiseDomain() {
        final DomainRegistry domainRegistry = problem.initialiseDomain();
        assertEquals(19, domainRegistry.getBuiltRepresentation().size());
    }
}
