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
package net.sourceforge.cilib.nn.architecture;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.components.Neuron;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class LayerTest {

    private Layer layer;
    private Vector refActivations;

    @Before
    public void setup() {
        layer = new Layer();
        layer.setBias(false);
        Neuron neuron = new Neuron();
        refActivations = Vector.of(-0.1, 0.7, 0.3, -0.5);
        for (int i = 0; i < refActivations.size(); i++) {
            neuron.setActivation(refActivations.doubleValueOf(i));
            layer.add((Neuron) neuron.getClone());
        }
    }

    @Test
    public void testGetNeuralInput() {
        for (int i = 0; i < refActivations.size(); i++) {
            Assert.assertEquals(refActivations.get(i).doubleValue(), layer.getNeuralInput(i), Maths.EPSILON);
        }
    }

    @Test
    public void testGetActivations() {
         Assert.assertEquals(refActivations, layer.getActivations());
    }

    @Test
    public void testIsBias() {
        Assert.assertEquals(false, layer.isBias());
    }
}
