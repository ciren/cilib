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
package net.sourceforge.cilib.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;



/**
 * This Unit test tests all the needed operations of the Queue container class.
 *
 * @author Gary Pampara
 */
public class BitArrayTest {

    @Test
    public void testBitArrayCreation() {
        BitArray b = new BitArray();
        assertNotNull(b);
    }

    @Test
    public void testBitArrayGet() {
        BitArray b = new BitArray(); // default with 32 bits
        assertEquals(false, b.get(10));
    }

    @Test
    public void testBitArraySet() {
        BitArray b = new BitArray(); // default with 32 bits

        b.set(2);
        assertEquals(true, b.get(2));
        assertEquals(false, b.get(30));
        b.set(30);
        assertEquals(true, b.get(30));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void lowerBound() {
        BitArray b = new BitArray();
        b.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void upperBound() {
        BitArray b = new BitArray();
        b.get(33);
    }

    @Test
    public void testLargerBitArray() {
        BitArray b = new BitArray(40);

        b.set(40);
        assertEquals(true, b.get(40));
    }

    @Test
    public void testBitArrayClear() {
        BitArray b = new BitArray(40);

        assertEquals(false, b.get(35));
        b.set(35);
        assertEquals(true, b.get(35));
        b.clear(35);
        assertEquals(false, b.get(35));
    }

    @Test
    public void testBitArraySizeInMemory() {
        BitArray b = new BitArray(50);

        int size = (50 >>> 5) + 1;
        assertEquals(size, b.size());
    }

    @Test
    public void testBitArrayBitLength() {
        BitArray b = new BitArray(60);
        assertEquals(60, b.length());
    }
}
