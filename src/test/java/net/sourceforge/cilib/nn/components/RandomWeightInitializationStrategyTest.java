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

import net.sourceforge.cilib.type.types.Real;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class RandomWeightInitializationStrategyTest {

    @Test
    public void testInitialize() {
        Neuron neuron = new Neuron();
        for (int i = 0; i < 10; i++) {
            neuron.getWeights().add(new Real(-5, 5));
        }
        RandomWeightInitializationStrategy initializationStrategy = new RandomWeightInitializationStrategy();
        initializationStrategy.initialize(neuron.getWeights());

        for (int i = 0; i < neuron.getWeights().size(); i++) {
            Assert.assertTrue(Double.compare(neuron.getWeights().get(i).getReal(),neuron.getWeights().get(i).getBounds().getUpperBound()) <= 0);
            Assert.assertTrue(Double.compare(neuron.getWeights().get(i).getReal(),neuron.getWeights().get(i).getBounds().getLowerBound()) >= 0);
        }
    }

}
