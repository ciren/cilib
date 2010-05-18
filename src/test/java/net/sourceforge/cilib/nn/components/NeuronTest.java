/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.NeuralInputSource;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class NeuronTest {

    private Neuron neuron;

    @Before
    public void setupNeuron() {
        neuron = new Neuron();
        Vector weights = Vectors.create(0.1, 0.2, 0.3, 0.4, 0.5);
        neuron.setWeights(weights);
        neuron.setActivationFunction(new Sigmoid());
    }

    @Test
    public void testActivation() {
        NeuralInputSource source = new NeuralInputSource() {

            Vector input = Vectors.create(0.5, 0.4, 0.3, 0.2, 0.1);

            @Override
            public double getNeuralInput(int index) {
                return input.getReal(index);
            }

            @Override
            public int size() {
                return 5;
            }

            public Vector getInput() {
                return input;
            }
        };
        Vector input = Vectors.create(0.5, 0.4, 0.3, 0.2, 0.1); // duplicate of vector above
        Vector weights = neuron.getWeights();
        double dotP = input.dot(weights);
        Sigmoid sigmoid = new Sigmoid();
        double reference = sigmoid.evaluate(dotP);
        Assert.assertEquals(reference, neuron.calculateActivation(source), Maths.EPSILON);
    }

    @Test
    public void testClone() {
        Neuron clone = new Neuron(neuron);
        Assert.assertEquals(neuron.getActivation(), clone.getActivation(), Maths.EPSILON);
        Assert.assertEquals(neuron.getActivationFunction(), clone.getActivationFunction());
        Assert.assertEquals(neuron.getWeights(), clone.getWeights());
    }
}
