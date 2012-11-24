/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import fj.data.List;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class StatsTestsTest {

    @Test
    public void testFriedmanAndPostHoc() {
        Iterable<? extends Iterable<Double>> a = Arrays.asList(
            Arrays.asList(9.3,9.4,9.2,9.7),
            Arrays.asList(9.4,9.3,9.4,9.6),
            Arrays.asList(9.6,9.8,9.5,10.0),
            Arrays.asList(10.0,9.9,9.7,10.2)
        );
        Double expResult = 8.8462;
        Double result = StatsTests.friedman(0.05, a)._1();
        assertEquals(expResult, result, 0.0001);
        
        Iterable<Integer> expLResult = Arrays.asList(0,1,2);
        assertEquals(List.iterableList(expLResult), List.iterableList(StatsTests.postHoc(0.05, result, a)));
    }
}
