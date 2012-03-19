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

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class RandomWeightInitializationStrategyTest {

    @Test
    public void testInitialize() {
        Neuron neuron = new Neuron();
        for (int i = 0; i < 10; i++) {
            neuron.getWeights().add(Real.valueOf(0.0, new Bounds(-5, 5)));
        }
        RandomWeightInitializationStrategy initializationStrategy = new RandomWeightInitializationStrategy();
        initializationStrategy.initialize(neuron.getWeights());

        for (int i = 0; i < neuron.getWeights().size(); i++) {
            Assert.assertTrue(Double.compare(neuron.getWeights().get(i).doubleValue(),neuron.getWeights().get(i).getBounds().getUpperBound()) <= 0);
            Assert.assertTrue(Double.compare(neuron.getWeights().get(i).doubleValue(),neuron.getWeights().get(i).getBounds().getLowerBound()) >= 0);
        }
    }

}
