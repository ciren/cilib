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

import org.junit.Test;

/**
 *
 * @author Edwin Peer
 */
public class TauswortheTest {

    public TauswortheTest() {

    }


    @Test
    public void testNextDouble() {
        RandomTester tester = new SimpleRandomTester();
        Random r = new Tausworthe();
        for (int i = 0; i < 100000; ++i) {
            double d = r.nextDouble();
            assertTrue("Random value out of range", 0 <= d && d < 1);
            tester.addSample(d);
        }
        assertTrue("Samples are not random", tester.hasRandomSamples());
    }

}
