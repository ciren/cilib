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
package net.sourceforge.cilib.container;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;


import java.util.List;
import net.sourceforge.cilib.type.types.Int;

import org.junit.Test;

public class SortedListTest {

    @Test
    public void addition() {
        SortedList<Int> intList = new SortedList<Int>();

        intList.add(Int.valueOf(5));
        intList.add(Int.valueOf(8));
        intList.add(Int.valueOf(2));

        assertEquals(2, intList.get(0).intValue());
        assertEquals(5, intList.get(1).intValue());
        assertEquals(8, intList.get(2).intValue());
    }

    /**
     * This test determines that the element set at the specified index will
     * be changed, but in order to maintain the sorted structure of the list,
     * the list is immediately sorted again to make sure that the elements
     * remain sorted.
     */
    @Test
    public void setValue() {
        SortedList<Int> list = new SortedList<Int>();

        list.add(Int.valueOf(3));
        list.add(Int.valueOf(1));
        list.add(Int.valueOf(12));

        list.set(0, Int.valueOf(4));

        assertEquals(3, list.get(0).intValue());
        assertEquals(4, list.get(1).intValue());
        assertEquals(12, list.get(2).intValue());
    }

    @Test
    public void addCollection() {
        SortedList<Int> list = new SortedList<Int>();

        list.add(Int.valueOf(0));
        list.add(Int.valueOf(200));

        List<Int> v = new ArrayList<Int>();
        v.add(Int.valueOf(4));
        v.add(Int.valueOf(1));
        v.add(Int.valueOf(50));

        list.addAll(list.size() - 1, v);

        assertEquals(0, list.get(0).intValue());
        assertEquals(1, list.get(1).intValue());
        assertEquals(4, list.get(2).intValue());
        assertEquals(50, list.get(3).intValue());
        assertEquals(200, list.get(4).intValue());
    }
}
