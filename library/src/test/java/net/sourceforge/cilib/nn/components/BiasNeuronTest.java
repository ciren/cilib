/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.NeuralInputSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class BiasNeuronTest {

    private BiasNeuron neuron;

    @Before
    public void setup() {
        neuron = new BiasNeuron();
    }

    @Test
    public void testCalculateActivation() {
        NeuralInputSource source = null;
        Assert.assertEquals(-1.0, neuron.getActivation(), Maths.EPSILON);

        double result = neuron.calculateActivation(source);
        Assert.assertEquals(-1.0, result, Maths.EPSILON);
        Assert.assertEquals(-1.0, neuron.getActivation(), Maths.EPSILON);
    }
}
