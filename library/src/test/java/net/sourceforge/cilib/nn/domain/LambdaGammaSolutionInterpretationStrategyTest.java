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
    public void shouldInitialize() {
        LambdaGammaSolutionConversionStrategy lambdaGammaSolutionInterpretationStrategy = new LambdaGammaSolutionConversionStrategy();
        lambdaGammaSolutionInterpretationStrategy.initialize(neuralNetwork);
        assertEquals(11, lambdaGammaSolutionInterpretationStrategy.getWeightCount());
        assertEquals(3, lambdaGammaSolutionInterpretationStrategy.getActivationFuncCount());
    }
}
