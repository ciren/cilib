/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class RandomWeightInitialisationStrategyTest {

    @Test
    public void testInitialise() {
        Neuron neuron = new Neuron();
        Vector.Builder v = Vector.newBuilder().copyOf(neuron.getWeights());

        for (int i = 0; i < 10; i++) {
            v.add(Real.valueOf(0.0, new Bounds(-5, 5)));
        }
        neuron.setWeights(v.build());

        RandomWeightInitialisationStrategy initialisationStrategy = new RandomWeightInitialisationStrategy();
        initialisationStrategy.initialise(neuron.getWeights());

        for (int i = 0; i < neuron.getWeights().size(); i++) {
            Assert.assertTrue(Double.compare(neuron.getWeights().get(i).doubleValue(),neuron.getWeights().get(i).getBounds().getUpperBound()) <= 0);
            Assert.assertTrue(Double.compare(neuron.getWeights().get(i).doubleValue(),neuron.getWeights().get(i).getBounds().getLowerBound()) >= 0);
        }
    }

}
