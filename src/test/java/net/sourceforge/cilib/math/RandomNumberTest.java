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
package net.sourceforge.cilib.math;

import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.math.random.RandomNumber;

import org.junit.Test;

/**
 *
 * @author Gary Pampara
 *
 */
public class RandomNumberTest {

    @Test
    public void testGuassian() {
        RandomNumber rand = new RandomNumber();

        for (int i = 0; i < 1000; i++) {
            double number = rand.getGaussian();
            assertTrue(-5.0 < number);
            assertTrue(number < 5.0);
        }
    }

    @Test
    public void testUniform() {
        RandomNumber rand = new RandomNumber();

        for (int i = 0; i < 200; i++) {
            double number = rand.getUniform();
            assertTrue(number <= 1.0);
            assertTrue(0.0 <= number);
        }
    }
}
