/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NeuralNetworksTest {

    private NeuralNetwork neuralNetwork;

    @Before
    public void setup() {
        neuralNetwork = NeuralNetworksTestHelper.createFFNN(3, 4, 2);
    }

    @Test
    public void shouldCountWeights() {
        assertEquals(26, NeuralNetworks.countWeights(neuralNetwork));
    }

    @Test
    public void shouldCountActivationFunctions() {
        assertEquals(6, NeuralNetworks.countActivationFunctions(neuralNetwork));
    }
}
