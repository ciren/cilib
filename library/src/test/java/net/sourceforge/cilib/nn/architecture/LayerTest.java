/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
	
    @Test
    public void testGetClone() {
        Layer newLayer = layer.getClone();
        for (int i = 0; i < refActivations.size(); i++) {
            Assert.assertEquals(refActivations.get(i).doubleValue(), newLayer.getNeuralInput(i), Maths.EPSILON);
        }
    }
}
