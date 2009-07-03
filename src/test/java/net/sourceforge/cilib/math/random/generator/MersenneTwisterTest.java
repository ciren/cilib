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
package net.sourceforge.cilib.math.random.generator;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Edwin Peer
 */
public class MersenneTwisterTest {

    public MersenneTwisterTest() {

    }

    @Test
    public void testNextDouble() {
        RandomTester tester = new SimpleRandomTester();
        Random r = new MersenneTwister();
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
        Random r = new MersenneTwister(5489L);

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
