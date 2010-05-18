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
package net.sourceforge.cilib.util.selection.ordering;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.util.selection.Selection;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 *
 * @author gpampara
 */
public class RingBasedOrderingTest {

    @Test
    public void ringBasedOrdering() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Selection.Entry<Integer>> entries = Selection.from(elements).entries();
        boolean ordered = new RingBasedOrdering<Integer>(5).order(entries);
        Assert.assertThat(ordered, is(true));

        Assert.assertThat(entries.get(0).getElement(), is(6));
        Assert.assertThat(entries.get(1).getElement(), is(7));
        Assert.assertThat(entries.get(2).getElement(), is(8));
        Assert.assertThat(entries.get(3).getElement(), is(9));
        Assert.assertThat(entries.get(4).getElement(), is(1));
        Assert.assertThat(entries.get(5).getElement(), is(2));
        Assert.assertThat(entries.get(6).getElement(), is(3));
        Assert.assertThat(entries.get(7).getElement(), is(4));
        Assert.assertThat(entries.get(8).getElement(), is(5));
    }
}
