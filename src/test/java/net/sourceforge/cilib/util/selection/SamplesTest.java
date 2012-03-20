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
package net.sourceforge.cilib.util.selection;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class SamplesTest {

    @Test
    public void first() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.first());

        Assert.assertEquals(1, result.get(0).intValue());
    }

    @Test
    public void firstN() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.first(2));

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(1, result.get(0).intValue());
        Assert.assertEquals(1, result.get(1).intValue());
    }

    @Test
    public void last() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.last());

        Assert.assertEquals(3, result.get(0).intValue());
    }

    @Test
    public void lastN() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.last(2));

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(3, result.get(0).intValue());
        Assert.assertEquals(3, result.get(1).intValue());
    }

    @Test
    public void all() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.all());

        Assert.assertEquals(6, result.size());
        Assert.assertArrayEquals(ints.toArray(), result.toArray());
    }

    @Test
    public void allSingleInstance() {
        Samples predicate1 = Samples.all();
        Samples predicate2 = Samples.all();

        Assert.assertSame(predicate1, predicate2);
    }

    @Test
    public void lastNUniqueSample() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.last(3).unique());

        Assert.assertArrayEquals(new Integer[]{3, 2, 1}, result.toArray());
    }

    @Test
    public void lastUniqueSample() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.last().unique());

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(3, result.get(0).intValue());
    }

    @Test
    public void firstUnique() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.first().unique());

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(1, result.get(0).intValue());
    }

    @Test
    public void firstNUnique() {
        List<Integer> ints = Lists.newArrayList(1, 1, 2, 2, 3, 3);
        List<Integer> result = Selection.copyOf(ints).select(Samples.first(3).unique());

        Assert.assertEquals(1, result.get(0).intValue());
        Assert.assertEquals(2, result.get(1).intValue());
        Assert.assertEquals(3, result.get(2).intValue());
    }
}
