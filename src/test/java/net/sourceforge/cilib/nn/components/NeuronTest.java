/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
 * @author andrich
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
        };
        Vector input = Vector.of(0.5, 0.4, 0.3, 0.2, 0.1); // duplicate of vector above
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
