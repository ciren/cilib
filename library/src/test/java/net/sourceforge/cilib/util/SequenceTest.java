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
package net.sourceforge.cilib.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class SequenceTest {

    @Test
    public void rangeSize() {
        Iterable<Number> range = Sequence.finiteRange(0, 100);
        Assert.assertTrue(Iterables.size(range) == 101);
    }

    @Test
    public void rangeContents() {
        List<Number> rangeList = ImmutableList.copyOf(Sequence.finiteRange(0, 100));
        Assert.assertEquals(0, rangeList.get(0).intValue());
        Assert.assertEquals(99, rangeList.get(99).intValue());
    }

    @Test
    public void repeat() {
        List<Number> repeated = ImmutableList.copyOf(Sequence.of(0).withFiniteSizeOf(30));
        Assert.assertEquals(30, repeated.size());
        for (int i = 0; i < 30; i++) {
            Assert.assertEquals(0, repeated.get(i));
        }
    }

//    @Test
//    public void sequenceByIncrement() {
//        List<Number> rangeBy3 = ImmutableList.copyOf(Sequence.range(1, 100).by(3));
//        Assert.assertEquals(34, rangeBy3.size());
//    }
}
