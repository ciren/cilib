/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.domain;

import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.nn.NeuralNetworksTestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LambdaGammaSolutionInterpretationStrategyTest {

    private NeuralNetwork neuralNetwork;

    @Before
    public void setup() {
        neuralNetwork = NeuralNetworksTestHelper.createFFNN(3, 2, 1);
    }

    @Test
    public void shouldInitialise() {
        LambdaGammaSolutionConversionStrategy lambdaGammaSolutionInterpretationStrategy = new LambdaGammaSolutionConversionStrategy();
        lambdaGammaSolutionInterpretationStrategy.initialise(neuralNetwork);
        assertEquals(11, lambdaGammaSolutionInterpretationStrategy.getWeightCount());
        assertEquals(3, lambdaGammaSolutionInterpretationStrategy.getActivationFuncCount());
    }
}
