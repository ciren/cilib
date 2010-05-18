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
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * @author gpampara
 */
public class SortedOrderingTest {

    @Test
    public void sortedOrdering() {
        List<Integer> elements = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        List<Selection.Entry<Integer>> entries = Selection.from(elements).entries();
        boolean ordered = new SortedOrdering<Integer>().order(entries);
        Assert.assertTrue(ordered);

        for (int i = 0; i < 9; ++i) {
            Assert.assertThat(entries.get(i).getElement(), is(equalTo(i + 1)));
        }
    }
}
