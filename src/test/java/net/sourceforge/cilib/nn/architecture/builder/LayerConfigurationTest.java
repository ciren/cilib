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

package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.functions.activation.ActivationFunction;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class LayerConfigurationTest {

    @Test
    public void testConfiguration() {
        boolean hasBias = true;
        boolean doesnotHaveBias = false;
        int size = 5;
        ActivationFunction activationFunction = new Sigmoid();

        LayerConfiguration layerConfiguration = new LayerConfiguration();
        Assert.assertEquals(hasBias, layerConfiguration.isBias());
        Assert.assertEquals(0, layerConfiguration.getSize());
        Assert.assertEquals(activationFunction.getClass(), layerConfiguration.getActivationFunction().getClass());

        layerConfiguration = new LayerConfiguration(size);
        Assert.assertEquals(hasBias, layerConfiguration.isBias());
        Assert.assertEquals(size, layerConfiguration.getSize());
        Assert.assertEquals(activationFunction.getClass(), layerConfiguration.getActivationFunction().getClass());

        layerConfiguration = new LayerConfiguration(size,doesnotHaveBias);
        Assert.assertEquals(doesnotHaveBias, layerConfiguration.isBias());
        Assert.assertEquals(size, layerConfiguration.getSize());
        Assert.assertEquals(activationFunction.getClass(), layerConfiguration.getActivationFunction().getClass());

        layerConfiguration = new LayerConfiguration(size,activationFunction);
        Assert.assertEquals(hasBias, layerConfiguration.isBias());
        Assert.assertEquals(size, layerConfiguration.getSize());
        Assert.assertEquals(activationFunction.getClass(), layerConfiguration.getActivationFunction().getClass());

        layerConfiguration = new LayerConfiguration(size,activationFunction,doesnotHaveBias);
        Assert.assertEquals(doesnotHaveBias, layerConfiguration.isBias());
        Assert.assertEquals(size, layerConfiguration.getSize());
        Assert.assertEquals(activationFunction.getClass(), layerConfiguration.getActivationFunction().getClass());
    }
}
