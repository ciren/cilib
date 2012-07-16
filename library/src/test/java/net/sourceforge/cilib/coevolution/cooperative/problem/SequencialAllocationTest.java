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

import org.junit.Test;

public class SequencialAllocationTest {
    @Test
    public void SequencialTest(){
        SequencialDimensionAllocation allocation = new SequencialDimensionAllocation(0, 2);
        assertEquals(2, allocation.getSize(), 0.0);
        assertEquals(0, allocation.getProblemIndex(0), 0.0);
        assertEquals(1, allocation.getProblemIndex(1), 0.0);
        allocation = new SequencialDimensionAllocation(2, 2);
        assertEquals(2, allocation.getSize(), 0.0);
        assertEquals(2, allocation.getProblemIndex(0), 0.0);
        assertEquals(3, allocation.getProblemIndex(1), 0.0);
    }
}
