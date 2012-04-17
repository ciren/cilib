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
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.nn.architecture.Layer;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PrototypeFullyConnectedLayerBuilderTest {

    @Test
    public void testBuildLayer() {
        int layerSize = 5;
        int previousLayerSize = 4;
        LayerConfiguration configuration1 = new LayerConfiguration(layerSize);
        LayerConfiguration configuration2 = new LayerConfiguration(layerSize, false);

        PrototypeFullyConnectedLayerBuilder builder = new PrototypeFullyConnectedLayerBuilder();
        builder.setDomain("R(-3:3)");
        Layer layer = builder.buildLayer(configuration1, previousLayerSize);

        Assert.assertEquals(layerSize + 1, layer.size());
        for (int i = 0; i < layerSize; i++) {
            Assert.assertEquals(previousLayerSize, layer.get(i).getWeights().size());
            Assert.assertEquals(-3.0,
                    layer.get(i).getWeights().get(0).getBounds().getLowerBound(),
                    Maths.EPSILON);
            Assert.assertEquals(3.0,
                    layer.get(i).getWeights().get(0).getBounds().getUpperBound(),
                    Maths.EPSILON);
        }

        layer = builder.buildLayer(configuration2, previousLayerSize);

        Assert.assertEquals(layerSize, layer.size());
        for (int i = 0; i < layerSize; i++) {
            Assert.assertEquals(previousLayerSize, layer.get(i).getWeights().size());
            Assert.assertEquals(-3.0,
                    layer.get(i).getWeights().get(0).getBounds().getLowerBound(),
                    Maths.EPSILON);
            Assert.assertEquals(3.0,
                    layer.get(i).getWeights().get(0).getBounds().getUpperBound(),
                    Maths.EPSILON);
        }
    }
}
