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
package net.cilib.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class CandidateSolutionTest {

    @Test
    public void builder() {
        CandidateSolution solution  = CandidateSolution.newBuilder()
                .add(0).add(1).add(2).add(3).add(4).build();

        Assert.assertArrayEquals(new double[]{0, 1, 2, 3, 4}, solution.toArray(), 0.0001);
    }

    @Test
    public void defensiveToArray() {
        CandidateSolution solution = CandidateSolution.copyOf(1.0, 1.0);
        double [] a = solution.toArray();
        double [] b = solution.toArray();

        Assert.assertNotSame(a, b);
        Assert.assertArrayEquals(a, b, 0.001);
    }
}