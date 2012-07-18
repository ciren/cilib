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
 *
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

}
