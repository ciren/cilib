/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.nn.architecture.builder.CascadeArchitectureBuilder;
import net.sourceforge.cilib.nn.architecture.builder.LayerConfiguration;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNDataTrainingProblem;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NNLayerCountTest {

    @Test
    public void theTest() {
        NNLayerCount measurement = new NNLayerCount();

        NNDataTrainingProblem problem = new NNDataTrainingProblem();
        final Algorithm algorithm = mock(Algorithm.class);
        when(algorithm.getOptimisationProblem()).thenReturn(problem);

        NeuralNetwork network1 = new NeuralNetwork();
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(5));
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network1.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(6));
        network1.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network1.initialise();
        problem.setNeuralNetwork(network1);

        assertEquals(4, measurement.getValue(algorithm).intValue());

        NeuralNetwork network2 = new NeuralNetwork();
        network2.getArchitecture().setArchitectureBuilder(new CascadeArchitectureBuilder());
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(3));
        network2.getArchitecture().getArchitectureBuilder().addLayer(new LayerConfiguration(2));
        network2.getArchitecture().getArchitectureBuilder().getLayerBuilder().setDomain("R(-3:3)");
        network2.initialise();
        problem.setNeuralNetwork(network2);

        assertEquals(3, measurement.getValue(algorithm).intValue());
    }
}
