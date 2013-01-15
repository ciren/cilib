/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BitTest {

    @Test
    public void testClone() {
        Bit b = Bit.valueOf(false);
        Bit clone = b.getClone();

        assertNotSame(b, clone);
        assertTrue(b.equals(clone));
    }

    @Test
    public void testEquals() {
        Bit b1 = Bit.valueOf(false);
        Bit b2 = Bit.valueOf(false);
        Bit b3 = Bit.valueOf(true);
        Bit b4 = Bit.valueOf(true);

        assertTrue(b1.equals(b1));
        assertTrue(b2.equals(b2));
        assertTrue(b3.equals(b3));
        assertTrue(b4.equals(b4));

        assertTrue(b1.equals(b2));
        assertTrue(b3.equals(b4));
        assertFalse(b2.equals(b3));
        assertFalse(b1.equals(b4));
    }

    @Test
    public void testGet() {
        Bit b1 = Bit.valueOf(true);
        Bit b2 = Bit.valueOf(false);

        assertEquals(true, b1.booleanValue());
        assertEquals(false, b2.booleanValue());
    }

    @Test
    public void testSet() {
//        Bit b1 = Bit.valueOf(true);
//        Bit b2 = Bit.valueOf(false);

        Bit b1 = Bit.valueOf(false);
        Bit b2 = Bit.valueOf(true);

        assertEquals(false, b1.booleanValue());
        assertEquals(true, b2.booleanValue());
    }

    @Test
    public void testCompareTo() {
        Bit b1 = Bit.valueOf(true);
        Bit b2 = Bit.valueOf(false);

        assertEquals(0, b1.compareTo(b1));
        assertEquals(-1, b2.compareTo(b1));
        assertEquals(1, b1.compareTo(b2));
    }

    @Test
    public void testRandomize() {
        Bit b1 = Bit.valueOf(true);
        Bit b2 = Bit.valueOf(true);
        b2.randomise();

        if (b2.booleanValue()) {
            assertTrue(b1.booleanValue() == b2.booleanValue());
        } else {
            assertTrue(b1.booleanValue() != b2.booleanValue());
        }
    }

 }
