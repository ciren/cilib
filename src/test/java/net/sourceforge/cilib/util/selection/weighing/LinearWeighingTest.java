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
package net.sourceforge.cilib.util.selection.weighing;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.Selection.Entry;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class LinearWeighingTest {

    @Test
    public void linearWeighing() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Entry<Integer>> weighedElements = Selection.from(elements).weigh(new LinearWeighing<Integer>(0.0,1.0)).entries();
        Assert.assertEquals(0.0,    weighedElements.get(0).getWeight(), 0.0001);
        Assert.assertEquals(0.125,  weighedElements.get(1).getWeight(), 0.0001);
        Assert.assertEquals(0.25,   weighedElements.get(2).getWeight(), 0.0001);
        Assert.assertEquals(0.375,  weighedElements.get(3).getWeight(), 0.0001);
        Assert.assertEquals(0.5,    weighedElements.get(4).getWeight(), 0.0001);
        Assert.assertEquals(0.625,  weighedElements.get(5).getWeight(), 0.0001);
        Assert.assertEquals(0.75,   weighedElements.get(6).getWeight(), 0.0001);
        Assert.assertEquals(0.875,  weighedElements.get(7).getWeight(), 0.0001);
        Assert.assertEquals(1.0,    weighedElements.get(8).getWeight(), 0.0001);
    }

}
