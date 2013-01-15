/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.problem;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SequentialAllocationTest {
    @Test
    public void SequencialTest(){
        SequentialDimensionAllocation allocation = new SequentialDimensionAllocation(0, 2);
        assertEquals(2, allocation.getSize(), 0.0);
        assertEquals(0, allocation.getProblemIndex(0), 0.0);
        assertEquals(1, allocation.getProblemIndex(1), 0.0);
        allocation = new SequentialDimensionAllocation(2, 2);
        assertEquals(2, allocation.getSize(), 0.0);
        assertEquals(2, allocation.getProblemIndex(0), 0.0);
        assertEquals(3, allocation.getProblemIndex(1), 0.0);
    }
}
