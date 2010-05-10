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

        intList.add(new Int(5));
        intList.add(new Int(8));
        intList.add(new Int(2));

        assertEquals(2, intList.get(0).getInt());
        assertEquals(5, intList.get(1).getInt());
        assertEquals(8, intList.get(2).getInt());
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

        list.add(new Int(3));
        list.add(new Int(1));
        list.add(new Int(12));

        list.set(0, new Int(4));

        assertEquals(3, list.get(0).getInt());
        assertEquals(4, list.get(1).getInt());
        assertEquals(12, list.get(2).getInt());
    }

    @Test
    public void addCollection() {
        SortedList<Int> list = new SortedList<Int>();

        list.add(new Int(0));
        list.add(new Int(200));

        List<Int> v = new ArrayList<Int>();
        v.add(new Int(4));
        v.add(new Int(1));
        v.add(new Int(50));

        list.addAll(list.size() - 1, v);

        assertEquals(0, list.get(0).getInt());
        assertEquals(1, list.get(1).getInt());
        assertEquals(4, list.get(2).getInt());
        assertEquals(50, list.get(3).getInt());
        assertEquals(200, list.get(4).getInt());
    }
}
