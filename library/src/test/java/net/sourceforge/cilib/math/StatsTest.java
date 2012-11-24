/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import java.util.Arrays;
import java.util.Iterator;
import static org.junit.Assert.*;
import org.junit.Test;

public class StatsTest {

    @Test
    public void testMean() {
        Iterable<Double> a = Arrays.asList(1.0, 2.0, 3.0);
        Double expResult = 2.0;
        Double result = Stats.mean(a);
        assertEquals(expResult, result);
    }

    @Test
    public void testMedian() {
        Iterable<Double> a = Arrays.asList(1.0, 3.0, 1.0, 6.0);
        Double expResult = 2.0;
        Double result = Stats.median(a);
        assertEquals(expResult, result);
        
        a = Arrays.asList(1.0, 9.0, 3.0);
        expResult = 3.0;
        result = Stats.median(a);
        assertEquals(expResult, result);
    }

    @Test
    public void testVariance() {
        Iterable<Double> a = Arrays.asList(2.0,4.0,4.0,4.0,5.0,5.0,7.0,9.0);
        Double expResult = 4.0;
        Double result = Stats.variance(a);
        assertEquals(expResult, result);
    }

    @Test
    public void testStdDev() {
        Iterable<Double> a = Arrays.asList(2.0,4.0,4.0,4.0,5.0,5.0,7.0,9.0);
        Double expResult = 2.0;
        Double result = Stats.stdDev(a);
        assertEquals(expResult, result);
    }

    @Test
    public void testRank() {
        Iterable<Double> a = Arrays.asList(1.0, 6.0, 2.0);
        Iterable<Double> expResult = Arrays.asList(1.0, 3.0, 2.0);
        Iterable result = Stats.rank(a);
        
        Iterator<Double> i = result.iterator();
        for (Double e : expResult) {
            assertEquals(e, i.next());
        }
        
        a = Arrays.asList(9.0, 6.0, 2.0, 4.0, 2.0);
        expResult = Arrays.asList(5.0, 4.0, 1.5, 3.0, 1.5);
        result = Stats.rank(a);
        
        i = result.iterator();
        for (Double e : expResult) {
            assertEquals(e, i.next());
        }
    }
}
