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
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

import net.sourceforge.cilib.util.Vectors;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Gary Pampara
 */
public class VectorTest {
    private static Vector vector;
    private static Vector tmpVector;

    @BeforeClass
    public static void setUp() {
        vector = new Vector();

        for(int i = 1; i < 5; i++) {
            Numeric element = new Real(i);
            element.setBounds(i*-2, i*2);
            vector.append(element);
        }
    }

    @AfterClass
    public static void tearDown() {
        vector = null;
        tmpVector = null;
    }

    private void recreateTmpVector() {
        tmpVector = new Vector();
        tmpVector.append(new Real(1.0));
        tmpVector.append(new Real(2.0));
        tmpVector.append(new Real(3.0));
    }

    @Test
    public void testClone() {
        Vector v = vector.getClone();

        assertEquals(v.size(), vector.size());

        for (int i = 0; i < vector.getDimension(); i++) {
            assertEquals(vector.getReal(i), v.getReal(i), 0.0);
            assertNotSame(vector.get(i), v.get(i));
        }
    }

    @Test
    public void testSet() {
        vector.setReal(0, 3.0);
        assertEquals(3.0, vector.getReal(0), 0.0);
        vector.setReal(0, 1.0);
        assertEquals(1.0, vector.getReal(0), 0.0);
    }

    @Test
    public void testNumericGet() {
        recreateTmpVector();

        assertEquals(1.0, tmpVector.getReal(0), 0.0);
        assertEquals(2.0, tmpVector.getReal(1), 0.0);
        assertEquals(3.0, tmpVector.getReal(2), 0.0);

        Real t = (Real) tmpVector.get(0);

        assertEquals(1.0, t.getReal(), 0.0);
    }

    @Test
    public void testNumericSet() {
        assertEquals(1.0, vector.getReal(0), 0.0);
        vector.setReal(0, 99.9);
        assertEquals(99.9, vector.getReal(0), 0.0);

        vector.setInt(0, 2);
        assertEquals(2, vector.getInt(0), 0.0);

        vector.setReal(0, 1.0);
        assertEquals(1.0, vector.getReal(0), 0.0);
    }

    @Test
    public void testDimension() {
        assertFalse(vector.getDimension() == 3);
        assertTrue(vector.getDimension() == 4);
        assertFalse(vector.getDimension() == 5);
    }

    @Test
    public void testInsert() {
        Vector m = new Vector();
        double [] targetResults = { 0.0, 1.0, 2.0, 3.0, 4.0 };

        m.add(new Real(1.0));
        m.add(new Real(3.0));
        assertEquals(2, m.getDimension());

        m.insert(1, new Real(2.0));
        assertEquals(3, m.getDimension());

        m.insert(0, new Real(0.0));
        assertEquals(4, m.getDimension());

        for (int i = 0; i < 4; i++) {
            assertEquals(targetResults[i], m.getReal(i), 0.0);
        }
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void invalidIndexInsert() {
        vector.insert(6, new Real(1.0));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void negativeIndexInsert() {
        vector.insert(-1, new Real(1.0));
    }

    @Test
    public void testRemove() {
        Vector m = new Vector();

        m.add(new Real(1.0));
        m.add(new Real(2.2));

        assertEquals(2, m.getDimension());

        m.remove(1);
        assertEquals(1.0, m.getReal(0), 0.0);
        assertEquals(1, m.getDimension());
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void removeNegativeIndex() {
        // Invalid indexes
        vector.remove(-1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void removeIndexToLarge() {
        vector.remove(10);
    }

    @Test
    public void testAdd() {
        Vector m = new Vector();
        assertEquals(0, m.getDimension());

        m.add(new Real(1.0));
        assertEquals(1, m.getDimension());
    }

    @Test
    public void testGetReal() {
        Object tmp = vector.getReal(0);
        assertTrue(tmp instanceof Double);
    }

    @Test
    public void testSetReal() {
        Vector m = new Vector();
        m.add(new Real(-10.0, 10.0));
        m.setReal(0, 10.0);

        assertEquals(10.0, m.getReal(0), 0.0);
    }

    @Test
    public void testGetInt() {
        Object tmp = vector.getInt(0);
        assertTrue(tmp instanceof Integer);
    }

    @Test
    public void testSetInt() {
        Vector m = new Vector();
        m.add(new Int(2));
        assertEquals(2, m.getInt(0));
        m.setInt(0, 5);
        assertEquals(5, m.getInt(0));

        m.add(new Real(-99.99));
        m.setInt(1, 1);
        assertEquals(1, m.getInt(1));

        m.add(new Bit());
        m.setBit(2, true);
        assertTrue(m.getBit(2));
    }

    @Test
    public void testGetBit() {
        Object tmp = vector.getBit(0);
        assertTrue(tmp instanceof Boolean);
    }

    @Test
    public void testSetBit() {
        Vector m = new Vector();
        m.add(new Bit());
        m.setBit(0, false);

        assertFalse(m.getBit(0));
    }

    @Test
    public void randomize() {
        Vector target = new Vector();
        target.add(new Real(1.0));
        target.add(new Real(2.0));
        target.add(new Real(3.0));
        target.randomize(new MersenneTwister());

        assertFalse(target.getReal(0) == 1.0);
        assertFalse(target.getReal(1) == 2.0);
        assertFalse(target.getReal(2) == 3.0);
    }

    @Test
    public void testVectorNorm() {
        Vector m = new Vector();

        m.add(new Real(1.0));
        m.add(new Real(1.0));
        m.add(new Real(1.0));
        m.add(new Real(1.0));
        m.add(new Real(1.0));
        assertEquals(sqrt(5.0), m.norm(), 0.0);

        m.clear();

        m.add(new Real(2.0));
        m.add(new Real(-2.0));
        m.add(new Real(2.0));
        m.add(new Real(-2.0));
        m.add(new Real(2.0));
        m.add(new Real(-2.0));
        assertEquals(sqrt(24.0), m.norm(), 0.0);
    }

    @Test
    public void testVectorDotProduct() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.add(new Real(1.0));
        v1.add(new Real(2.0));
        v1.add(new Real(3.0));

        v2.add(new Real(3.0));
        v2.add(new Real(2.0));
        v2.add(new Real(1.0));

        assertEquals(10.0, v1.dot(v2), 0.0);

        v2.setReal(0, -3.0);
        assertEquals(4.0, v1.dot(v2), 0.0);
    }

    @Test
    public void vectorCrossProduct() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.add(new Real(1.0));
        v1.add(new Real(2.0));
        v1.add(new Real(3.0));

        v2.add(new Real(4.0));
        v2.add(new Real(5.0));
        v2.add(new Real(6.0));

        Vector result = v1.cross(v2);

        assertEquals(-3.0, result.getReal(0), 0);
        assertEquals(6.0, result.getReal(1), 0);
        assertEquals(-3.0, result.getReal(2), 0);
    }

    @Test(expected = ArithmeticException.class)
    public void invalidVectorCrossProduct() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.add(new Real(1.0));
        v1.add(new Real(2.0));
        v1.add(new Real(3.0));

        v2.add(new Real(4.0));
        v2.add(new Real(5.0));
        v2.add(new Real(6.0));
        v2.add(new Real(7.0));

        v1.cross(v2);
    }

    @Test(expected = ArithmeticException.class)
    public void invalidVectorLengthCrossPorduct() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.add(new Real(1.0));
        v1.add(new Real(2.0));

        v2.add(new Real(4.0));
        v2.add(new Real(7.0));

        v1.cross(v2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invalidVectorAddition() {
        Vector a = new Vector();
        Vector b = new Vector();

        for(int i = 0; i < 10; i++)
            a.append(new Real(i));
        for(int i = 0; i < 9; i++)
            b.prepend(new Real(i));

        a.plus(b);
    }

    @Test
    public void testPlus() {
        Vector a = new Vector();
        Vector b = new Vector();

        for(int i = 0; i < 10; i++)
            a.append(new Real(i));
        for(int i = 0; i < 9; i++)
            b.prepend(new Real(i));

        b.prepend(new Real(9));
        Vector sum = a.plus(b);

        assertNotNull(sum);
        assertNotSame(a, b);
        assertNotSame(sum, a);
        assertNotSame(sum, b);

        for(int i = 0; i < 10; i++) {
            assertNotSame(a.get(i), b.get(i));
            assertNotSame(sum.get(i), a.get(i));
            assertNotSame(sum.get(i), b.get(i));

            assertEquals(a.getReal(i), Integer.valueOf(i).doubleValue(), 0.0);
            assertEquals(b.getReal(i), Double.valueOf(9.0 - i), 0.0);
            assertEquals(sum.getReal(i), 9.0, 0.0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invalidVectorsubtraction() {
        Vector a = new Vector();
        Vector b = new Vector();

        for(int i = 0; i < 10; i++)
            a.append(new Real(i));
        for(int i = 0; i < 9; i++)
            b.prepend(new Real(i));

        a.subtract(b);
    }

    @Test
    public void testSubtract() {
        Vector a = new Vector();
        Vector b = new Vector();

        for(int i = 0; i < 10; i++)
            a.append(new Real(i));
        for(int i = 0; i < 9; i++)
            b.prepend(new Real(i));

        b.prepend(new Real(9));
        Vector difference = a.subtract(b);

        assertNotNull(a);
        assertNotNull(b);
        assertNotNull(difference);
        assertNotSame(a, b);
        assertNotSame(difference, a);
        assertNotSame(difference, b);

        for(int i = 0; i < 10; i++) {
            assertNotNull(a.get(i));
            assertNotNull(b.get(i));
            assertNotNull(difference.get(i));
            assertNotSame(a.get(i), b.get(i));
            assertNotSame(difference.get(i), a.get(i));
            assertNotSame(difference.get(i), b.get(i));

            assertEquals(a.getReal(i), (double)i, 0.0);
            assertEquals(b.getReal(i), (9.0 - i), 0.0);
            assertEquals(difference.getReal(i), (i - (9.0 - i)), 0.0);
        }
    }

    @Test(expected = ArithmeticException.class)
    public void vectorDivisionByScalarZero() {
        Vector a = new Vector();

        for(int i = 0; i < 10; i++)
            a.append(new Real(i));

        a.divide(0);
    }

    @Test
    public void testScalarDivision() {
        Vector a = new Vector();

        for(int i = 0; i < 10; i++)
            a.append(new Real(i));

        Vector divided = a.divide(3.0);

        assertNotNull(a);
        assertNotNull(divided);
        assertNotSame(divided, a);

        for(int i = 0; i < 10; i++) {
            assertNotNull(a.get(i));
            assertNotNull(divided.get(i));
            assertNotSame(divided.get(i), a.get(i));

            assertEquals(a.getReal(i), (double) i, 0.0);
            assertEquals(divided.getReal(i), (i / 3.0), 0.000000001);
        }
    }

    @Test
    public void testScalarMultiplication() {
        Vector a = new Vector();

        for(int i = 0; i < 10; i++)
            a.append(new Real(i));

        Vector product = a.multiply(3.0);

        assertNotNull(a);
        assertNotNull(product);
        assertNotSame(product, a);

        for(int i = 0; i < 10; i++) {
            assertNotNull(a.get(i));
            assertNotNull(product.get(i));
            assertNotSame(product.get(i), a.get(i));

            assertEquals(a.getReal(i), (double)i, 0.0);
            assertEquals(product.getReal(i), (i * 3.0), 0.0);
        }
    }

    @Test
    public void equals() {
        Vector a = new Vector();
        Vector b = new Vector();

        a.add(new Real(1.0));
        a.add(new Real(2.0));

        b.add(new Real(1.0));
        b.add(new Real(2.0));

        assertFalse(a.equals(null));
        assertTrue(b.equals(b));
        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
    }

    @Test
    public void subList() {
        Vector original = Vectors.create(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Vector subVector = original.subList(0, 3);

        Assert.assertEquals(4, subVector.size());
    }
}
