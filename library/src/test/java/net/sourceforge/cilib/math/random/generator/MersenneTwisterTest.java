/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator;

import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MersenneTwisterTest {

    @Test
    public void testNextDouble() {
        RandomTester tester = new SimpleRandomTester();
        MersenneTwister r = new MersenneTwister(Rand.nextLong());
        for (int i = 0; i < 100000; ++i) {
            double d = r.nextDouble();
            assertTrue("Random value out of range", 0 <= d && d < 1);
            tester.addSample(d);
        }
        assertTrue("Samples are not random", tester.hasRandomSamples());
    }

    /**
     * TODO: check the number of digits after the decimal point....
     * should it be more?
     */
    @Test
    public void sequence() {
        MersenneTwister r = new MersenneTwister(5489L);

        Assert.assertEquals(0.81472369, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.13547700, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.90579193, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.83500858, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.12698681, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.96886777, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.91337586, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.22103404, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.63235925, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.30816705, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.09754040, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.54722060, r.nextDouble(), 0.00000001);
        Assert.assertEquals(0.27849822, r.nextDouble(), 0.00000001);
    }
}
