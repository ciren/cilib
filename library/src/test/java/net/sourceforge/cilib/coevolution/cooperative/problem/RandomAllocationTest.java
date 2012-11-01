/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
