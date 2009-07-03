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
package net.sourceforge.cilib.functions.discrete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gary Pampara
 *
 */
public class BinaryAdapterTest {

    private static BinaryAdapter adapter;

    @BeforeClass
    public static void setUp() {
        adapter = new BinaryAdapter();
        adapter.setFunction(new Rastrigin());
    }

    /**
     * Cleanup
     */
    @AfterClass
    public static void tearDown() {
        adapter = null;
    }

    /**
     * Test the process of setting the bits per dimension.
     */
    @Test(expected = RuntimeException.class)
    public void testDimensionSettings() {
        adapter.setBitsPerDimension(5);
        assertEquals(5, adapter.getBitsPerDimension());

        adapter.setBitsPerDimension(0);
        assertEquals(0, adapter.getBitsPerDimension());

        adapter.setBitsPerDimension(-9);
    }


    /**
     * Test the process of creating a bit vector and decoding it into real values.
     */
    @Test
    public void testSimpleDecoding() {
        adapter.setBitsPerDimension(4);
        adapter.setPrecision(0);

        Vector bitVector = new Vector();
        for (int i = 0; i < 4; i++) {
            bitVector.add(new Bit(true));
        }

        assertEquals(4, bitVector.getDimension());

        Vector converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());

        Type t = converted.get(0);
        assertTrue(t instanceof Numeric);
        Numeric n = (Numeric) t;
        assertEquals(15.0, n.getReal(), Double.MIN_NORMAL);

        bitVector.setBit(3, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(14.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(2, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(12.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(1, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(8.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(0, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(0.0, converted.getReal(0), Double.MIN_NORMAL);
    }


    @Test
    public void testSimpleDoubleDecoding() {
        adapter.setBitsPerDimension(4);

        Vector bitVector = new Vector();
        for (int i = 0; i < 8; i++) {
            bitVector.add(new Bit(true));
        }

        assertEquals(8, bitVector.getDimension());

        Vector converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.getDimension());

        Type t = converted.get(0);
        assertTrue(t instanceof Numeric);
        Numeric n = (Numeric) t;
        assertEquals(15.0, n.getReal(), Double.MIN_NORMAL);

        bitVector.setBit(3, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.getDimension());
        assertEquals(14.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(2, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.getDimension());
        assertEquals(12.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(1, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.getDimension());
        assertEquals(8.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(0, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.getDimension());
        assertEquals(0.0, converted.getReal(0), Double.MIN_NORMAL);
    }

    @Test
    public void testComplex() {
        adapter.setBitsPerDimension(8);

        Vector bitVector = new Vector();
        for (int i = 0; i < 8; i++) {
            bitVector.add(new Bit(true));
        }

        assertEquals(8, bitVector.getDimension());

        Vector converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());

        Type t = converted.get(0);
        assertTrue(t instanceof Numeric);
        Numeric n = (Numeric) t;
        assertEquals(255.0, n.getReal(), Double.MIN_NORMAL);

        bitVector.setBit(3, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(239.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(2, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(207.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(1, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(143.0, converted.getReal(0), Double.MIN_NORMAL);

        bitVector.setBit(0, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.getDimension());
        assertEquals(15.0, converted.getReal(0), Double.MIN_NORMAL);
    }

}
