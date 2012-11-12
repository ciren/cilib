/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

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

        Vector.Builder bitVectorB = Vector.newBuilder();
        for (int i = 0; i < 4; i++) {
            bitVectorB.add(Bit.valueOf(true));
        }
        
        Vector bitVector = bitVectorB.build();

        assertEquals(4, bitVector.size());

        Vector converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());

        Type t = converted.get(0);
        assertTrue(t instanceof Numeric);
        Numeric n = (Numeric) t;
        assertEquals(15.0, n.doubleValue(), Double.MIN_NORMAL);

        bitVector.setBit(3, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(14.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(2, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(12.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(1, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(8.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(0, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(0.0, converted.doubleValueOf(0), Double.MIN_NORMAL);
    }


    @Test
    public void testSimpleDoubleDecoding() {
        adapter.setBitsPerDimension(4);

        Vector.Builder bitVectorB = Vector.newBuilder();
        for (int i = 0; i < 8; i++) {
            bitVectorB.add(Bit.valueOf(true));
        }
        
        Vector bitVector = bitVectorB.build();

        assertEquals(8, bitVector.size());

        Vector converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.size());

        Type t = converted.get(0);
        assertTrue(t instanceof Numeric);
        Numeric n = (Numeric) t;
        assertEquals(15.0, n.doubleValue(), Double.MIN_NORMAL);

        bitVector.setBit(3, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.size());
        assertEquals(14.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(2, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.size());
        assertEquals(12.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(1, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.size());
        assertEquals(8.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(0, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(2, converted.size());
        assertEquals(0.0, converted.doubleValueOf(0), Double.MIN_NORMAL);
    }

    @Test
    public void testComplex() {
        adapter.setBitsPerDimension(8);

        Vector.Builder bitVectorB = Vector.newBuilder();
        for (int i = 0; i < 8; i++) {
            bitVectorB.add(Bit.valueOf(true));
        }
        
        Vector bitVector = bitVectorB.build();

        assertEquals(8, bitVector.size());

        Vector converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());

        Type t = converted.get(0);
        assertTrue(t instanceof Numeric);
        Numeric n = (Numeric) t;
        assertEquals(255.0, n.doubleValue(), Double.MIN_NORMAL);

        bitVector.setBit(3, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(239.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(2, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(207.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(1, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(143.0, converted.doubleValueOf(0), Double.MIN_NORMAL);

        bitVector.setBit(0, false);
        converted = adapter.decodeBitString(bitVector);
        assertEquals(1, converted.size());
        assertEquals(15.0, converted.doubleValueOf(0), Double.MIN_NORMAL);
    }

}
