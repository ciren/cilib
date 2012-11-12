/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 */
public class RandomInitialiserTest {

    public RandomInitialiserTest() {

    }

    @BeforeClass
    public static void setUp() {
/*        initialiser = new RandomInitialiser(new Random());
        parser = new Parser(initialiser);*/
    }

    @Test
    public void testBit() {
    /*    int ones = 0;
        int zeroes = 0;
        for (int i = 0; i < 100; ++i) {
            initialiser.reset();
            parser.build("B");
            //Bit tmp = (Bit) initialiser.getResult();
            Bit tmp = (Bit) initialiser.getBuiltRepresentation();
            if (tmp.getBit()) {
                ++ones;
            }
            else {
                ++zeroes;
            }
        }
        assertTrue(ones > 10);
        assertTrue(zeroes > 10);*/
    }

    @Test
    public void testInt() {
        /*int total = 0;
        for (int i = 0; i < 100; ++i) {
            initialiser.reset();
            parser.build("Z(0, 100)");
            //Int tmp = (Int) initialiser.getResult();
            Int tmp = (Int) initialiser.getBuiltRepresentation();
            assertTrue(tmp.getInt() <= 100);
            assertTrue(tmp.getInt() >= 0);
            total += tmp.getInt();
        }
        assertTrue(total >= 10);*/
    }

    @Test
    public void testReal() {
        /*double total = 0;
        for (int i = 0; i < 100; ++i) {
            initialiser.reset();
            parser.build("R(0, 1)");
            //Real tmp = (Real) initialiser.getResult();
            Real tmp = (Real) initialiser.getBuiltRepresentation();
            assertTrue(tmp.getReal() >= 0);
            assertTrue(tmp.getReal() <= 1);
            total += tmp.getReal();
        }
        assertTrue(total > 10);*/
    }

    @Test
    public void testPrefixBitVector() {
        /*parser.build("[B,B,B,B,B]");
        //BitVector tmp = (BitVector) initialiser.getResult();
        BitVector tmp = (BitVector) initialiser.getBuiltRepresentation();
        assertEquals(5, tmp.getDimension());*/
    }

    @Test
    public void testPostfixBitVector() {
        /*parser.build("B^128");
        //BitVector tmp = (BitVector) initialiser.getResult();
        BitVector tmp = (BitVector) initialiser.getBuiltRepresentation();
        assertEquals(128, tmp.getDimension());*/
    }

    @Test
    public void testPostfixBitVectorWithSlack() {
/*        int total = 0;
        for (int i = 0; i < 100; ++i) {
            initialiser.reset();
            parser.build("B^10~5");
            //BitVector tmp = (BitVector) initialiser.getResult();
            BitVector tmp = (BitVector) initialiser.getBuiltRepresentation();
            assertTrue(tmp.getDimension() >= 10);
            assertTrue(tmp.getDimension() <= 15);
            total += tmp.getDimension();
        }
        assertFalse(total == 1000);
        assertFalse(total == 1500);*/
    }

    @Test
    public void testPrefixRealVector() {
/*        parser.build("[R, R]");

        //MixedVector tmp = (MixedVector) initialiser.getResult();
        MixedVector tmp = (MixedVector) initialiser.getBuiltRepresentation();
        assertEquals(2, tmp.getDimension());*/
    }

    @Test
    public void testPostfixRealVector() {
        /*parser.build("R(1, 10)^50");

        //MixedVector tmp = (MixedVector) initialiser.getResult();
        MixedVector tmp = (MixedVector) initialiser.getBuiltRepresentation();
        assertEquals(50, tmp.getDimension());
        double total = 0;
        for (int i = 0; i < 50; ++i) {
            assertTrue(tmp.getReal(i) >= 1);
            assertTrue(tmp.getReal(i) <= 10);
            total += tmp.getReal(i);
        }
        assertFalse(total == 50);
        assertFalse(total == 500);*/
    }

    @Test
    public void testMixedRealBitVector() {
/*        parser.build("[R(-1, 1)^30, B^20]");
        //Vector tmp = (Vector) initialiser.getResult();
        Vector tmp = (Vector) initialiser.getBuiltRepresentation();
        assertEquals(50, tmp.getDimension());

        for (int i = 0; i < 30; ++i) {
            assertTrue(tmp.get(i) instanceof Real);
            assertTrue(tmp.getReal(i) >= -1 && tmp.getReal(i) <= 1);
        }

        for (int i = 31; i < 50; ++i) {
            assertTrue(tmp.get(i) instanceof Bit);
        }*/
    }

    @Test
    public void testFlattenComposite() {
        /*parser.build("[R(0,1),R,R^3,[[R,R], [R^10],R,R]^2,R]");
        //Vector tmp = (Vector) initialiser.getResult();
        Vector tmp = (Vector) initialiser.getBuiltRepresentation();
        assertEquals(34, tmp.getDimension());

        assertTrue(tmp.getReal(0) >= 0 && tmp.getReal(0) <= 1);

        for (int i = 0; i < tmp.getDimension(); ++i) {
            assertTrue(tmp.get(i) instanceof Real);
            assertFalse(Double.isNaN(tmp.getReal(i)));
            assertFalse(Double.isInfinite(tmp.getReal(i)));
        }*/
    }

    @Test
    public void testAlternatingComposite() {
        /*parser.build("[R,B]^5");
        //Vector tmp = (Vector) initialiser.getResult();
        Vector tmp = (Vector) initialiser.getBuiltRepresentation();
        assertEquals(10, tmp.getDimension());

        for (int i = 0; i < 10; i +=2) {
            assertTrue(tmp.get(i) instanceof Real);
        }

        for (int i = 1; i < 10; i +=2) {
            assertTrue(tmp.get(i) instanceof Bit);
        }*/
    }

    @Test
    public void testRealMatrix() {
        /*parser.build("R^3^3");
        //Vector tmp = (Vector) initialiser.getResult();
        Vector tmp = (Vector) initialiser.getBuiltRepresentation();
        assertEquals(9, tmp.getDimension());

        for (int i = 0; i < tmp.getDimension(); ++i) {
            assertTrue(tmp.get(i) instanceof Real);
            assertFalse(Double.isNaN(tmp.getReal(i)));
            assertFalse(Double.isInfinite(tmp.getReal(i)));
        }
         */
    }

    @Test
    public void testPathologicalCase() {
        /*parser.build("[[R,B^10]^4^3, [R, [R], [[R]], [Z,R, [R, R,[R, [[R]]]]^2], B],R,R,B^13,Z(-10,10)^3 ]^10");
        //Vector tmp = (Vector) initialiser.getResult();
        Vector tmp = (Vector) initialiser.getBuiltRepresentation();
        assertEquals(1640, tmp.getDimension()); // ((11 * 4 * 3) + 5 + 8 + 1 + 2 + 13 + 3) * 10 = 1680
        */
        // TODO: Check the contents
    }

//    private Parser parser;
//    private RandomInitialiser initialiser;
}
