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
package net.sourceforge.cilib.util.selection.ordering;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.SelectionSyntax;
import net.sourceforge.cilib.util.selection.weighing.LinearWeighing;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 *
 * @author gpampara
 */
public class ProportionalOrderingTest {

    @Test
    public void proportionalOrdering() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<SelectionSyntax.Entry<Integer>> entries = Selection.from(elements).weigh(new LinearWeighing<Integer>(1, 9)).entries();
        boolean ordered = new ProportionalOrdering<Integer>(new ConstantRandomNumber()).order(entries);
        Assert.assertThat(ordered, is(true));

        // Random numbers in the range [0,1) will be generated in the following order:
        // 0.730967787376657
        // 0.24053641567148587
        // 0.6374174253501083
        // 0.5504370051176339
        // 0.5975452777972018
        // 0.3332183994766498
        // 0.3851891847407185
        // 0.984841540199809
        // 0.8791825178724801

        // Initial total sum is 45.
        // First marker at: 45 * 0.730967787376657 = 32.8935504316 -> 8
        // Order: (8|, 1, 2, 3, 4, 5, 6, 7, 9)
        Assert.assertThat(entries.get(8).getElement(), is(8));

        // Total sum is 45 - 8 = 37.
        // First marker at: 37 * 0.24053641567148587 = 8.89984737979 -> 4
        // Order: (8, 4|, 1, 2, 3, 5, 6, 7, 9)
        Assert.assertThat(entries.get(7).getElement(), is(4));

        // Total sum is 37 - 4 = 33.
        // First marker at: 33 * 0.6374174253501083 = 21.0347750366 -> 7
        // Order: (8, 4, 7|, 1, 2, 3, 5, 6, 9)
        Assert.assertThat(entries.get(6).getElement(), is(7));

        // Total sum is 33 - 7 = 26.
        // First marker at: 26 * 0.5504370051176339 = 14.3113621329 -> 6
        // Order: (8, 4, 7, 6|, 1, 2, 3, 5, 9)
        Assert.assertThat(entries.get(5).getElement(), is(6));

        // Total sum is 26 - 6 = 20.
        // First marker at: 20 * 0.5975452777972018 = 11.9509055558 -> 9
        // Order: (8, 4, 7, 6, 9|, 1, 2, 3, 5)
        Assert.assertThat(entries.get(4).getElement(), is(9));

        // Total sum is 20 - 9 = 11.
        // First marker at: 11 * 0.3332183994766498 = 3.66540239417 -> 3
        // Order: (8, 4, 7, 6, 9, 3|, 1, 2, 5)
        Assert.assertThat(entries.get(3).getElement(), is(3));

        // Total sum is 11 - 3 = 8.
        // First marker at: 8 * 0.3851891847407185 = 3.08151347792 -> 5
        // Order: (8, 4, 7, 6, 9, 3, 5|, 1, 2)
        Assert.assertThat(entries.get(2).getElement(), is(5));

        // Total sum is 8 - 5 = 3.
        // First marker at: 3 * 0.984841540199809 = 2.95452462057 -> 2
        // Order: (8, 4, 7, 6, 9, 3, 5, 2|, 1)
        Assert.assertThat(entries.get(1).getElement(), is(2));

        // Finally...
        Assert.assertThat(entries.get(0).getElement(), is(1));
    }

    private static class ConstantRandomNumber extends Random {

        private static final long serialVersionUID = 3019387660938987850L;

        public ConstantRandomNumber() {
            super(0);
        }

        @Override
        public Random getClone() {
            return this;
        }

        @Override
        public int nextInt(int n) {
            return super.nextInt(n);
        }
    }
}
