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
package net.sourceforge.cilib.util.selection;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author gpampara
 */
public class SelectionTest {

    @Test
    public void exclusionSelection() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<Integer> exlusionElements = Arrays.asList(1, 2, 4, 6);
        List<Integer> selection = Selection.from(elements).exclude(exlusionElements).select(Samples.first(3)).perform();
        Assert.assertEquals(3, selection.size());
        Assert.assertEquals(3, selection.get(0).intValue());
        Assert.assertEquals(5, selection.get(1).intValue());
        Assert.assertEquals(7, selection.get(2).intValue());
    }

    @Test
    public void predicateSelection() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Predicate<Integer> evenNumbers = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer element) {
                return element % 2 == 0;
            }
        };

        Predicate<Integer> numberSeven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer element) {
                return element == 7;
            }
        };

        List<Integer> selection = Selection.from(elements).filter(Predicates.<Integer>or(evenNumbers, numberSeven)).select(Samples.all()).perform();
        Assert.assertThat(selection.size(), is(4));
        Assert.assertThat(selection.get(0), is(2));
        Assert.assertThat(selection.get(1), is(4));
        Assert.assertThat(selection.get(2), is(6));
        Assert.assertThat(selection.get(3), is(7));
    }

    @Test
    public void randomFrom() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Integer selection = Selection.from(elements).random(new MockRandom()).select(Samples.first()).performSingle();
        Assert.assertEquals(1, selection.intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyRandomFrom() {
        List<Integer> elements = Arrays.<Integer>asList();
        Selection.from(elements).random(new MockRandom());
    }

    @Test
    public void multipleRandomFrom() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> selection = Selection.from(elements).random(new MockRandom(), 2).select(Samples.all()).perform();

        Assert.assertEquals(2, selection.size());
        Assert.assertThat(selection.get(0), is(1));
        Assert.assertThat(selection.get(1), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyMultipleRandomFrom() {
        List<Integer> elements = Arrays.<Integer>asList();
        Selection.from(elements).random(new MockRandom(), 2);
    }

    @Test
    public void uniqueSelection() {
        List<Integer> elements = Arrays.asList(1, 1, 1, 2, 2);
        List<Integer> randoms = Selection.from(elements).unique().random(new MockRandom(), 2).select(Samples.all()).perform();

        Assert.assertThat(randoms.size(), is(2));
        Assert.assertTrue(randoms.contains(1));
        Assert.assertTrue(randoms.contains(2));
    }

    @Test(expected=IllegalStateException.class)
    public void notEnoughUniqueElements() {
        List<Integer> elements = Arrays.asList(1, 1, 1, 2, 2);
        Selection.from(elements).unique().random(new MockRandom(), 3).select(Samples.all()).perform();
    }

    private class MockRandom implements RandomProvider {
        private static final long serialVersionUID = 6512653155066129236L;

        public MockRandom() {
        }

        @Override
        public RandomProvider getClone() {
            return this;
        }

        @Override
        public int nextInt(int n) {
            return 0;
        }

        @Override
        public boolean nextBoolean() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int nextInt() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long nextLong() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public float nextFloat() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double nextDouble() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextBytes(byte[] bytes) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
