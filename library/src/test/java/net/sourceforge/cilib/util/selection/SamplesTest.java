/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
