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
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * @author Gary Pampara
 */
public class BitTest {

    @Test
    public void testClone() {
        Bit b = new Bit(false);
        Bit clone = (Bit) b.getClone();

        assertNotSame(b, clone);
        assertTrue(b.equals(clone));
    }

    @Test
    public void testEquals() {
        Bit b1 = new Bit(false);
        Bit b2 = new Bit(false);
        Bit b3 = new Bit(true);
        Bit b4 = new Bit(true);

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
        Bit b1 = new Bit(true);
        Bit b2 = new Bit(false);

        assertEquals(true, b1.getBit());
        assertEquals(false, b2.getBit());
    }

    @Test
    public void testSet() {
        Bit b1 = new Bit(true);
        Bit b2 = new Bit(false);

        b1.setBit(false);
        b2.setBit(true);

        assertEquals(false, b1.getBit());
        assertEquals(true, b2.getBit());
    }

    @Test
    public void testCompareTo() {
        Bit b1 = new Bit(true);
        Bit b2 = new Bit(false);

        assertEquals(0, b1.compareTo(b1));
        assertEquals(-1, b2.compareTo(b1));
        assertEquals(1, b1.compareTo(b2));
    }

    @Test
    public void testRandomize() {
        Bit b1 = new Bit(true);
        Bit b2 = new Bit(true);
        b2.randomize(new MersenneTwister());

        if (b2.getBit())
            assertTrue(b1.getBit() == b2.getBit());
        else
            assertTrue(b1.getBit() != b2.getBit());
    }

 }
