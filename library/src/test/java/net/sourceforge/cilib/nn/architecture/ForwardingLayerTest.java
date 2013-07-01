/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.architecture;

import net.cilib.io.pattern.StandardPattern;
import net.cilib.math.Maths;
import net.cilib.nn.components.BiasNeuron;
import net.cilib.nn.components.PatternInputSource;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class ForwardingLayerTest {

    private ForwardingLayer layer;
    private Vector input;

    @Before
    public void setup() {
        input = Vector.of(0.1, 0.2, 0.3, 0.4, 0.5);
        StandardPattern pattern = new StandardPattern(input, input);
        layer = new ForwardingLayer();
        layer.setSource(new PatternInputSource(pattern));
        layer.add(new BiasNeuron());
        layer.add(new BiasNeuron());
        layer.add(new BiasNeuron());
    }

    @Test
    public void testNeuralInput() {
        Vector refInput = Vector.copyOf(input);
        refInput = Vector.newBuilder().copyOf(refInput).add(Real.valueOf(-1.0)).build();
        Assert.assertEquals(8, layer.size());

        for (int i = 0; i < refInput.size(); i++) {
            Assert.assertEquals(refInput.doubleValueOf(i), layer.getNeuralInput(i), Maths.EPSILON);
        }
    }

    @Test
    public void testGetActivations() {
        Vector refInput = Vector.copyOf(input);
        refInput = Vector.newBuilder().copyOf(refInput).add(Real.valueOf(-1.0)).build();
        refInput = Vector.newBuilder().copyOf(refInput).add(Real.valueOf(-1.0)).build();
        refInput = Vector.newBuilder().copyOf(refInput).add(Real.valueOf(-1.0)).build();
        Assert.assertEquals(refInput, layer.getActivations());
    }
}
