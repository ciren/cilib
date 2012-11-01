/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
