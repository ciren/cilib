/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.random.generator.Rand;
import static org.junit.Assert.*;
import org.junit.Test;

public class IntTest {

    @Test
    public void testClone() {
        Int i = Int.valueOf(-10);
        Int clone = i.getClone();

        assertEquals(i.intValue(), clone.intValue());
        assertNotSame(i, clone);
    }

    @Test
    public void testEquals() {
        Int i1 = Int.valueOf(10);
        Int i2 = Int.valueOf(10);
        Int i3 = Int.valueOf(-5);

        assertTrue(i1.equals(i1));
        assertTrue(i2.equals(i2));
        assertTrue(i3.equals(i3));

        assertTrue(i1.equals(i2));
        assertFalse(i1.equals(i3));
        assertTrue(i2.equals(i1));
        assertFalse(i2.equals(i3));
    }

    @Test
    public void testCompareTo() {
        Int i1 = Int.valueOf(15, new Bounds(0, 30));
        Int i2 = Int.valueOf(-15, new Bounds(-30, 0));

        assertEquals(0, i1.compareTo(i1));
        assertEquals(0, i2.compareTo(i2));
        assertEquals(1, i1.compareTo(i2));
        assertEquals(-1, i2.compareTo(i1));
    }

    @Test
    public void testRandomize() {
        Rand.setSeed(0);
        Int i1 = Int.valueOf(0, new Bounds(-300, 300));
        Int i2 = i1.getClone();

        assertTrue(i1.intValue() == i2.intValue());
        i1.randomise();
        assertTrue(i1.intValue() != i2.intValue());
    }
}
