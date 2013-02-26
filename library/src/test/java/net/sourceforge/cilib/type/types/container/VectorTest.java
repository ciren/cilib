/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import com.google.common.base.Predicate;
import fj.F;
import fj.F2;
import static java.lang.Math.sqrt;
import java.util.Arrays;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.math.random.generator.Rand;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class VectorTest {

    private static Vector vector;
    private static Vector tmpVector;

    @BeforeClass
    public static void setUp() {
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 1; i < 5; i++) {
            builder.addWithin(i, new Bounds(i * -2, i * 2));
        }
        vector = builder.build();
    }

    @AfterClass
    public static void tearDown() {
        vector = null;
        tmpVector = null;
    }

    private void recreateTmpVector() {
        Vector.Builder builder = Vector.newBuilder().add(Real.valueOf(1.0)).add(Real.valueOf(2.0)).add(Real.valueOf(3.0));
        tmpVector = builder.build();
    }

    @Test
    public void testClone() {
        Vector v = Vector.copyOf(vector);

        assertEquals(v.size(), vector.size());

        for (int i = 0; i < vector.size(); i++) {
            assertEquals(vector.doubleValueOf(i), v.doubleValueOf(i), 0.0);
            assertNotSame(vector.get(i), v.get(i));
        }
    }

    @Test
    public void testSet() {
        vector.setReal(0, 3.0);
        assertEquals(3.0, vector.doubleValueOf(0), 0.0);
        vector.setReal(0, 1.0);
        assertEquals(1.0, vector.doubleValueOf(0), 0.0);
    }

    @Test
    public void testNumericGet() {
        recreateTmpVector();

        assertEquals(1.0, tmpVector.doubleValueOf(0), 0.0);
        assertEquals(2.0, tmpVector.doubleValueOf(1), 0.0);
        assertEquals(3.0, tmpVector.doubleValueOf(2), 0.0);

        Real t = (Real) tmpVector.get(0);

        assertEquals(1.0, t.doubleValue(), 0.0);
    }

    @Test
    public void testNumericSet() {
        assertEquals(1.0, vector.doubleValueOf(0), 0.0);
        vector.setReal(0, 99.9);
        assertEquals(99.9, vector.doubleValueOf(0), 0.0);

        vector.setInt(0, 2);
        assertEquals(2, vector.intValueOf(0), 0.0);

        vector.setReal(0, 1.0);
        assertEquals(1.0, vector.doubleValueOf(0), 0.0);
    }

    @Test
    public void testDimension() {
        assertFalse(vector.size() == 3);
        assertTrue(vector.size() == 4);
        assertFalse(vector.size() == 5);
    }

    /*@Test
    // Add is deprecated
    public void testAdd() {
        Vector m = Vector.of();
        assertEquals(0, m.size());

        m.add(Real.valueOf(1.0));
        assertEquals(1, m.size());
    }*/

    @Test
    public void testGetReal() {
        Object tmp = vector.doubleValueOf(0);
        assertTrue(tmp instanceof Double);
    }

    @Test
    public void testSetReal() {
        Vector m = Vector.of(0);
        assertEquals(0.0, m.doubleValueOf(0), 0.0);

        m.setReal(0, 10.0);
        assertEquals(10.0, m.doubleValueOf(0), 0.0);
    }

    @Test
    public void testGetInt() {
        Object tmp = vector.intValueOf(0);
        assertTrue(tmp instanceof Integer);
    }

    @Test
    public void testSetInt() {
        Vector m = Vector.of(2);
        assertEquals(2, m.intValueOf(0));
        m.setInt(0, 5);
        assertEquals(5, m.intValueOf(0));

        m = Vector.newBuilder().copyOf(m).add(Real.valueOf(-99.99)).build();
        m.setInt(1, 1);
        assertEquals(1, m.intValueOf(1));

        m = Vector.newBuilder().copyOf(m).add(Bit.valueOf(true)).build();
        assertTrue(m.booleanValueOf(2));
    }

    @Test
    public void testGetBit() {
        Object tmp = vector.booleanValueOf(0);
        assertTrue(tmp instanceof Boolean);
    }

    @Test
    public void testSetBit() {
        Vector m = Vector.of(Bit.valueOf(false));

        assertFalse(m.booleanValueOf(0));
    }

    @Test
    public void randomise() {
        Vector target = Vector.newBuilder().add(Real.valueOf(1.0)).add(Real.valueOf(2.0)).add(Real.valueOf(3.0)).buildRandom();

        assertFalse(target.doubleValueOf(0) == 1.0);
        assertFalse(target.doubleValueOf(1) == 2.0);
        assertFalse(target.doubleValueOf(2) == 3.0);
    }

    @Test
    public void testVectorNorm() {
        Vector m = Vector.of(1.0, 1.0, 1.0, 1.0, 1.0);
        assertEquals(sqrt(5.0), m.norm(), 0.0);
    }

    @Test
    public void alternativeVectorNorm() {
        Vector m = Vector.of(2.0, -2.0, 2.0, -2.0, 2.0, -2.0);
        assertEquals(sqrt(24.0), m.norm(), 0.0);
    }

    @Test
    public void testVectorDotProduct() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0);
        Vector v2 = Vector.of(3.0, 2.0, 1.0);

        assertEquals(10.0, v1.dot(v2), 0.0);

        v2.setReal(0, -3.0);
        assertEquals(4.0, v1.dot(v2), 0.0);
    }

    @Test
    public void vectorCrossProduct() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0);
        Vector v2 = Vector.of(4.0, 5.0, 6.0);

        Vector result = v1.cross(v2);

        assertEquals(-3.0, result.doubleValueOf(0), 0);
        assertEquals(6.0, result.doubleValueOf(1), 0);
        assertEquals(-3.0, result.doubleValueOf(2), 0);
    }

    @Test(expected = ArithmeticException.class)
    public void invalidVectorCrossProduct() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0);
        Vector v2 = Vector.of(4.0, 5.0, 6.0, 7.0);

        v1.cross(v2);
    }

    @Test(expected = ArithmeticException.class)
    public void invalidVectorLengthCrossPorduct() {
        Vector v1 = Vector.of(1.0, 2.0);
        Vector v2 = Vector.of(4.0, 7.0);

        v1.cross(v2);
    }

    @Test(expected = ArithmeticException.class)
    public void vectorDivisionByScalarZero() {
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < 10; i++) {
            builder.add(Real.valueOf(i));
        }

        Vector a = builder.build();
        a.divide(0);
    }

    @Test
    public void testScalarDivision() {
        Vector.Builder builder = Vector.newBuilder();

        for (int i = 0; i < 10; i++) {
            builder.add(Real.valueOf(i));
        }

        Vector a = builder.build();
        Vector divided = a.divide(3.0);

        assertNotNull(a);
        assertNotNull(divided);
        assertNotSame(divided, a);

        for (int i = 0; i < 10; i++) {
            assertNotNull(a.get(i));
            assertNotNull(divided.get(i));
            assertNotSame(divided.get(i), a.get(i));

            assertEquals(a.doubleValueOf(i), (double) i, 0.0);
            assertEquals(divided.doubleValueOf(i), (i / 3.0), 0.000000001);
        }
    }

    @Test
    public void testScalarMultiplication() {
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < 10; i++) {
            builder.add(Real.valueOf(i));
        }

        Vector a = builder.build();
        Vector product = a.multiply(3.0);

        assertNotNull(a);
        assertNotNull(product);
        assertNotSame(product, a);

        for (int i = 0; i < 10; i++) {
            assertNotNull(a.get(i));
            assertNotNull(product.get(i));
            assertNotSame(product.get(i), a.get(i));

            assertEquals(a.doubleValueOf(i), (double) i, 0.0);
            assertEquals(product.doubleValueOf(i), (i * 3.0), 0.0);
        }
    }

    @Test
    public void equals() {
        Vector a = Vector.of(1.0, 2.0);
        Vector b = Vector.of(1.0, 2.0);

        assertFalse(a == null);
        assertTrue(b.equals(b));
        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
    }

    @Test
    public void subList() {
        Vector original = Vector.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Vector subVector = original.copyOfRange(0, 4);

        Assert.assertEquals(4, subVector.size());
    }

    @Test
    public void simpleBuilder() {
        Vector.Builder builder = Vector.newBuilder();
        builder.add(1.0);
        Vector result = builder.build();

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(1.0, result.doubleValueOf(0), 0.001);
    }

    @Test
    public void rangeBuilder() {
        Vector expected = Vector.newBuilder().add(0).add(1).add(2).build();
        Vector result = Vector.newBuilder().range(0, 3, 1).build();

        assertEquals(3, result.size());
        assertEquals(result, expected);
    }

    @Test
    public void copyOf() {
        final Vector initial = Vector.of(1.0, 1.0, 1.0);

        Vector.Builder builder = Vector.newBuilder();
        builder.copyOf(initial);
        Vector result = builder.build();

        Assert.assertEquals(3, result.size());
    }

    @Test
    public void emptyBuilder() {
        Vector.Builder builder = Vector.newBuilder();
        Vector result = builder.build();

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void filter() {
        Vector result = Vector.of(1.0, 2.0, 3.0).filter(new Predicate<Numeric>() {

            @Override
            public boolean apply(Numeric input) {
                if (Double.compare(input.doubleValue(), 2.0) != 0) {
                    return true;
                }
                return false;
            }
        });

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void foreach() {
        final Vector expected = Vector.of(2.0, 2.0, 2.0);
        Vector result = Vector.of(1.0, 1.0, 1.0).map(new F<Numeric, Numeric>() {
            @Override
            public Numeric f(Numeric x) {
                return Real.valueOf(x.doubleValue() * 2, x.getBounds());
            }
        });
        Assert.assertEquals(expected, result);
    }

    @Test
    public void foldLeft() {
        double result = Vector.of(1.0, 2.0, 3.0).foldLeft(4, new F<Numeric, Double>() {
            @Override
            public Double f(Numeric x) {
                return x.doubleValue();
            }
        });

        Assert.assertEquals(10.0, result, 0.0001);
    }

    @Test
    public void reduceLeft() {
        double result = Vector.of(1.0, 1.0, 1.0, 1.0).reduceLeft(new F2<Double, Double, Number>() {
            @Override
            public Double f(Double x, Double y) {
                return x.doubleValue() + y.doubleValue();
            }
        }).doubleValue();

        Assert.assertEquals(4.0, result, 0.0001);
    }

    @Test
    public void testIsZero() {
        Vector zero = Vector.of(0.0, 0.0, 0.0, 0.0, 0.0);
        Vector notZero = Vector.of(0.0, 0.0, 1.0, 0.0, 0.0);

        assertFalse(notZero.isZero());
        assertTrue(zero.isZero());
    }

    @Test
    public void testProject() {
        Vector u = Vector.of(2.0, 1.0);
        Vector v = Vector.of(-3.0, 4.0);

        assertEquals(u.project(v), Vector.of(6.0 / 25.0, -8.0 / 25.0));
    }

    @Test
    public void testOrthogonalize() {
        Vector v1 = Vector.of(3.0, 1.0);
        Vector v2 = Vector.of(2.0, 2.0);
        Vector ortho = v1.orthogonalize(Arrays.asList(v2));

        assertEquals(ortho.doubleValueOf(0), 1.0, 0.0);
        assertEquals(ortho.doubleValueOf(1), -1.0, 0.0);
    }

    @Test
    public void testPermute() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0, 4.0);
        Rand.setSeed(1);
        assertEquals(v1.permute(), Vector.of(2.0, 4.0, 3.0, 1.0));
    }

    @Test
    public void testSample() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0, 4.0);
        Rand.setSeed(1);

        assertEquals(Real.valueOf(2.0), v1.sample());
        assertEquals(Real.valueOf(4.0), v1.sample());
        assertEquals(Real.valueOf(3.0), v1.sample());
    }

    @Test
    public void testMaxAndMin() {
        Vector v1 = Vector.of(1.0, 2.0, 3.0, 4.0);

        assertEquals(Real.valueOf(4.0), v1.max());
        assertEquals(Real.valueOf(1.0), v1.min());
    }
}
