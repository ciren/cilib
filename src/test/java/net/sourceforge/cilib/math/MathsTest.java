/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author Gary Pampara, Theuns Cloete
 */
public class MathsTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidFactorialParameter() {
        Maths.factorial(-1);
    }

    @Test
    public void factorial() {
        Assert.assertEquals(1.0, Maths.factorial(0), Double.MIN_NORMAL);
        Assert.assertEquals(1.0, Maths.factorial(1), Double.MIN_NORMAL);
        Assert.assertEquals(6.0, Maths.factorial(3), Double.MIN_NORMAL);
        Assert.assertEquals(720.0, Maths.factorial(6), Double.MIN_NORMAL);
    }

    @Test(expected=IllegalArgumentException.class)
    public void factorialOverflow() {
        Maths.factorial(100);
    }

    @Test
    public void largeFactorial() {
        BigInteger n = Maths.largeFactorial(100);

        Assert.assertEquals("933262154439441526816992388562667004907159682643816214685929638952175999932299156089414639761565182862536979208272237582511852109168640000000000000000000000", n.toString());
    }

    @Test
    public void combination() {
        Assert.assertEquals(792.0, Maths.combination(12, 5), Double.MIN_NORMAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCombination() {
        Maths.combination(5, 12);
    }

    @Test(expected=IllegalArgumentException.class)
    public void combinationInvalidN() {
        Maths.combination(-1, 5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void combinationInvalidR() {
        Maths.combination(1, -5);
    }

    @Test
    public void combinationSpecialCase() {
        Assert.assertEquals(1.0, Maths.combination(0, 0), Double.MIN_NORMAL);
        Assert.assertEquals(1.0, Maths.combination(1, 0), Double.MIN_NORMAL);
        Assert.assertEquals(1.0, Maths.combination(1, 1), Double.MIN_NORMAL);
    }

    @Test
    public void listPermutation() {
        List<Integer> numbers = Arrays.asList(1, 2);
        List<List<Integer>> permutationList = new ArrayList<List<Integer>>();

        for (Iterator<List<Integer>> permutations = Maths.permutation(numbers, 2); permutations.hasNext(); ) {
            permutationList.add(permutations.next());
        }

        Assert.assertThat(permutationList.size(), is(2));

        List<Integer> expected1 = Arrays.asList(1, 2);
        List<Integer> expected2 = Arrays.asList(2, 1);
        Assert.assertTrue(permutationList.contains(expected1));
        Assert.assertTrue(permutationList.contains(expected2));
    }

    @Test
    public void flip() {
        RandomProvider provider = new MersenneTwister(1000L);

        Assert.assertThat(Maths.flip(0.0, provider), is(0));
        Assert.assertThat(Maths.flip(1.0, provider), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void flipNegativeProbability() {
        Maths.flip(-1.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void flipImpossibleProbability() {
        Maths.flip(1.1, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDownScaleTooSmall() {
        Assert.assertEquals(-2.4, Maths.scale(-10, 0, 100, -2, 2), Maths.EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDownScale() {
        Assert.assertEquals(-2.0, Maths.scale(0, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-1.6, Maths.scale(10, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-1.2, Maths.scale(20, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-0.8, Maths.scale(30, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-0.4, Maths.scale(40, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(0.0, Maths.scale(50, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(0.4, Maths.scale(60, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(0.8, Maths.scale(70, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(1.2, Maths.scale(80, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(1.6, Maths.scale(90, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(2.0, Maths.scale(100, 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(2.4, Maths.scale(110, 0, 100, -2, 2), Maths.EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testUpScaleTooSmall() {
        Assert.assertEquals(-10.0, Maths.scale(-2.4, -2, 2, 0, 100), Maths.EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testUpScale() {
        Assert.assertEquals(0.0, Maths.scale(-2.0, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(10.0, Maths.scale(-1.6, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(20.0, Maths.scale(-1.2, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(30.0, Maths.scale(-0.8, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(40.0, Maths.scale(-0.4, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(50.0, Maths.scale(0.0, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(60.0, Maths.scale(0.4, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(70.0, Maths.scale(0.8, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(80.0, Maths.scale(1.2, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(90.0, Maths.scale(1.6, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(100.0, Maths.scale(2.0, -2, 2, 0, 100), Maths.EPSILON);
        Assert.assertEquals(110.0, Maths.scale(2.4, -2, 2, 0, 100), Maths.EPSILON);
    }

    @Test
    public void testScale() {
        Assert.assertEquals(-2.0, Maths.scale(Maths.scale(-2.0, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-1.8, Maths.scale(Maths.scale(-1.8, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-1.6, Maths.scale(Maths.scale(-1.6, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-1.2, Maths.scale(Maths.scale(-1.2, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-0.8, Maths.scale(Maths.scale(-0.8, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(-0.4, Maths.scale(Maths.scale(-0.4, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(0.0, Maths.scale(Maths.scale(0.0, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(0.4, Maths.scale(Maths.scale(0.4, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(0.8, Maths.scale(Maths.scale(0.8, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(1.2, Maths.scale(Maths.scale(1.2, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(1.6, Maths.scale(Maths.scale(1.6, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
        Assert.assertEquals(2.0, Maths.scale(Maths.scale(2.0, -2, 2, 0, 100), 0, 100, -2, 2), Maths.EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNormalizeTooSmall() {
        Assert.assertEquals(-0.1, Maths.normalize(-60, -50, 50), Maths.EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNormalize() {
        Assert.assertEquals(0.0, Maths.normalize(-50, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.1, Maths.normalize(-40, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.2, Maths.normalize(-30, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.3, Maths.normalize(-20, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.4, Maths.normalize(-10, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.5, Maths.normalize(0, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.6, Maths.normalize(10, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.7, Maths.normalize(20, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.8, Maths.normalize(30, -50, 50), Maths.EPSILON);
        Assert.assertEquals(0.9, Maths.normalize(40, -50, 50), Maths.EPSILON);
        Assert.assertEquals(1.0, Maths.normalize(50, -50, 50), Maths.EPSILON);
        Assert.assertEquals(1.1, Maths.normalize(60, -50, 50), Maths.EPSILON);
    }
}
