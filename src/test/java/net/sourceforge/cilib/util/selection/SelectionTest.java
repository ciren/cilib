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

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class SelectionTest {

    @Test
    public void lastSelection() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> selection = Selection.from(elements).last(3).select();

        Assert.assertEquals(3, selection.size());
        Assert.assertTrue(selection.contains(7));
        Assert.assertTrue(selection.contains(8));
        Assert.assertTrue(selection.contains(9));

        selection = Selection.from(elements).last().select();
        Assert.assertEquals(1, selection.size());
        Assert.assertEquals(9, selection.get(0).intValue());
    }

    @Test
    public void firstSelection() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> selection = Selection.from(elements).first(3).select();

        Assert.assertEquals(3, selection.size());
        Assert.assertEquals(1, selection.get(0).intValue());
        Assert.assertEquals(2, selection.get(1).intValue());
        Assert.assertEquals(3, selection.get(2).intValue());

        selection = Selection.from(elements).first().select();
        Assert.assertEquals(1, selection.get(0).intValue());
        Assert.assertEquals(1, selection.size());
    }
}
