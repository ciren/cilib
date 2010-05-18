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
package net.sourceforge.cilib.coevolution.cooperative.problem;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class RandomAllocationTest {
    @Test
    public void AllocationTest(){

        List<Integer> indicies = Arrays.asList(1, 4, 8);

        RandomDimensionAllocation allocation = new RandomDimensionAllocation(indicies);
        assertEquals(3, allocation.getSize(), 0.0);
        assertEquals(1, allocation.getProblemIndex(0), 0.0);
        assertEquals(4, allocation.getProblemIndex(1), 0.0);
        assertEquals(8, allocation.getProblemIndex(2), 0.0);
    }
}
