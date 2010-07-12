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
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author gpampara
 */
public class RingBasedArrangementTest {

    @Test
    public void arrange() {
        List<Integer> elements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> ordered = Lists.newArrayList(new RingBasedArrangement(5).arrange(elements));
        Assert.assertTrue(!ordered.isEmpty());

        Assert.assertThat(ordered.get(0), is(6));
        Assert.assertThat(ordered.get(1), is(7));
        Assert.assertThat(ordered.get(2), is(8));
        Assert.assertThat(ordered.get(3), is(9));
        Assert.assertThat(ordered.get(4), is(1));
        Assert.assertThat(ordered.get(5), is(2));
        Assert.assertThat(ordered.get(6), is(3));
        Assert.assertThat(ordered.get(7), is(4));
        Assert.assertThat(ordered.get(8), is(5));
    }
}
