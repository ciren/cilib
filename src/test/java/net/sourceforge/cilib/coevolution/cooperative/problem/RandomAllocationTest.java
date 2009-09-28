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
