/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.builder;

import net.sourceforge.cilib.functions.activation.ActivationFunction;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import org.junit.Assert;
import org.junit.Test;

/**
 *
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
