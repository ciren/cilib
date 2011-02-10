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

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.util.selection.arrangement.Arrangement;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class WeightedSelectionTest {

    @Test(expected = NullPointerException.class)
    public void nullInput() {
        WeightedSelection.copyOf(null);
    }

    @Test
    public void creation() {
        List<Integer> ints = Lists.newArrayList(1, 2, 3);
        WeightedSelection<Integer> selection = WeightedSelection.copyOf(ints);
    }

    @Test
    public void select() {
        List<Integer> ints = Lists.newArrayList(1, 2, 3);
        Integer selection = WeightedSelection.copyOf(ints).select();
    }

    @Test
    public void exclude() {
        List<Integer> ints = Lists.newArrayList(1, 2, 3);
        List<Integer> result = WeightedSelection.copyOf(ints).exclude(1, 3).select(Samples.first());

        Assert.assertThat(result.get(0), is(2));
    }

    @Test
    public void filter() {
        List<Integer> ints = Lists.newArrayList(1, 2, 3);
        List<Integer> items = WeightedSelection.copyOf(ints).filter(new Predicate<Integer>() {

            @Override
            public boolean apply(Integer input) {
                return (input.intValue() % 2 == 0) ? true : false;
            }
        }).select(Samples.all());

        Assert.assertEquals(2, items.size());
    }

    @Test
    public void arrange() {
        List<Integer> ints = Lists.newArrayList(1, 2, 3);
        List<Integer> outcome = WeightedSelection.copyOf(ints).orderBy(new Arrangement<Integer>() {
            @Override
            public Iterable<Integer> arrange(Iterable<Integer> elements) {
                List<Integer> list = Lists.newArrayList(elements);
                Collections.reverse(list);
                return list;
            }
        }).select(Samples.all());

        Assert.assertArrayEquals(new Integer[]{3, 2, 1}, outcome.toArray());
    }
}
