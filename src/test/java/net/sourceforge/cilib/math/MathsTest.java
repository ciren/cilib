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
package net.sourceforge.cilib.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;


/**
 *
 * @author Gary Pampara
 */
public class MathsTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidFactorialParameter() {
        Maths.factorial(-1.0);
    }

    @Test
    public void testFactorial() {
        Assert.assertEquals(1.0, Maths.factorial(0.0), Double.MIN_NORMAL);
        Assert.assertEquals(1.0, Maths.factorial(1.0), Double.MIN_NORMAL);
        Assert.assertEquals(6.0, Maths.factorial(3), Double.MIN_NORMAL);
        Assert.assertEquals(720.0, Maths.factorial(6), Double.MIN_NORMAL);
        Assert.assertEquals(9.33262154439441E157, Maths.factorial(100), Double.MIN_NORMAL);
    }

    @Test
    public void testCombination() {
        Assert.assertEquals(792.0, Maths.combination(12, 5), Double.MIN_NORMAL);
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

}
