/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.NeuralInputSource;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class NeuronTest {

    private Neuron neuron;

    @Before
    public void setupNeuron() {
        neuron = new Neuron();
        Vector weights = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        neuron.setWeights(weights);
        neuron.setActivationFunction(new Sigmoid());
    }

    @Test
    public void testActivation() {
        NeuralInputSource source = new NeuralInputSource() {

            Vector input = Vector.of(0.5, 0.4, 0.3, 0.2, 0.1);

            @Override
            public double getNeuralInput(int index) {
                return input.doubleValueOf(index);
            }

            @Override
            public int size() {
                return 5;
            }

            public Vector getInput() {
                return input;
            }

			public Neuron getNeuron(int index) {
				Neuron neuron = new Neuron();
				neuron.setActivation(5);
				return neuron;
			}
        };
        Vector input = Vector.of(0.5, 0.4, 0.3, 0.2, 0.1); // duplicate of vector above
        Vector weights = neuron.getWeights();
        double dotP = input.dot(weights);
        Sigmoid sigmoid = new Sigmoid();
        double reference = sigmoid.apply(dotP);
        Assert.assertEquals(reference, neuron.calculateActivation(source), Maths.EPSILON);
    }

    @Test
    public void testClone() {
        Neuron clone = new Neuron(neuron);
        Assert.assertEquals(neuron.getActivation(), clone.getActivation(), Maths.EPSILON);
        Assert.assertEquals(neuron.getActivationFunction(), clone.getActivationFunction());
        Assert.assertEquals(neuron.getWeights(), clone.getWeights());

        clone.setActivationFunction(null);
        Neuron clone2 = clone.getClone();
        Assert.assertTrue(clone2.getActivationFunction() == null);
    }
}
