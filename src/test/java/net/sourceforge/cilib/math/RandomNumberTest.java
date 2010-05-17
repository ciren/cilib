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
package net.sourceforge.cilib.math;

import net.sourceforge.cilib.math.random.GaussianDistribution;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;

import net.sourceforge.cilib.math.random.UniformDistribution;
import org.junit.Test;

/**
 *
 * @author Gary Pampara
 *
 */
public class RandomNumberTest {

    private ProbabilityDistributionFuction rand;

    @Test
    public void testGuassian() {
        rand = new GaussianDistribution();

        for (int i = 0; i < 1000; i++) {
            double number = rand.getRandomNumber();
            assertTrue(-5.0 < number);
            assertTrue(number < 5.0);
        }
    }

    @Test
    public void testUniform() {
        rand = new UniformDistribution();

        for (int i = 0; i < 200; i++) {
            double number = rand.getRandomNumber();
            assertTrue(number <= 1.0);
            assertTrue(0.0 <= number);
        }
    }
}
