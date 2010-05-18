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
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.type.types.Int;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class TypeListTest {

    @Test
    public void subList() {
       TypeList list = new TypeList();
       list.add(new Vector());
       list.add(new BinaryTree());
       list.add(new Int(0));
       list.add(new Set());
       list.add(new NaryTree());

       TypeList subList = list.subList(0, 3);

       Assert.assertEquals(4, subList.size());
    }
}