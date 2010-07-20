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
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 *
 * @author gpampara
 */
public class RandomSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        RandomSelector<Integer> selection = new RandomSelector<Integer>();
        selection.on(elements).select();
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        RandomSelector<Integer> selection = new RandomSelector<Integer>();
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(1));
    }
}
