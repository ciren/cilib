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
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Selection;
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
        List<Selection.Entry<Integer>> entries = Selection.from(elements)
                .weigh(new LinearWeighing<Integer>(1, 9)).and().entries();
        boolean ordered = new ProportionalOrdering<Integer>(new ConstantRandomNumber()).order(entries);
        Assert.assertThat(ordered, is(true));

        // Random numbers in the range [0,1) will be generated in the following order:

        // 0.8147236874025613
        // 0.13547700503158466
        // 0.9057919358463374
        // 0.8350085784045876
        // 0.12698681606155282
        // 0.9688677774872758
        // 0.9133758577858514
        // 0.2210340445095188
        // 0.6323592410708524

        // Initial total sum is 45.
        // First marker at: 45 * 0.8147236874025613 = 36.662565933 -> 9
        // Order: (9|, 1, 2, 3, 4, 5, 6, 7, 8)
        Assert.assertThat(entries.get(8).getElement(), is(9));

        // Total sum is 45 - 9 = 36.
        // First marker at: 36 * 0.13547700503158466 = 4.87717218108 -> 3
        // Order: (9, 3|, 1, 2, 4, 5, 6, 7, 8)
        Assert.assertThat(entries.get(7).getElement(), is(3));

        // Total sum is 36 - 3 = 33.
        // First marker at: 33 * 0.9057919358463374 = 29.8911338827 -> 8
        // Order: (9, 3, 8|, 1, 2, 4, 5, 6, 7)
        Assert.assertThat(entries.get(6).getElement(), is(8));

        // Total sum is 33 - 8 = 25.
        // First marker at: 25 * 0.8350085784045876 = 20.87521446 -> 7
        // Order: (9, 3, 8, 7|, 1, 2, 4, 5, 6)
        Assert.assertThat(entries.get(5).getElement(), is(7));

        // Total sum is 25 - 7 = 18.
        // First marker at: 18 * 0.12698681606155282 = 2.28576268908 -> 2
        // Order: (9, 3, 8, 7, 2|, 1, 4, 5, 6)
        Assert.assertThat(entries.get(4).getElement(), is(2));

        // Total sum is 18 - 2 = 16.
        // First marker at: 16 * 0.9688677774872758 = 15.50188444 -> 6
        // Order: (9, 3, 8, 7, 2, 6|, 1, 4, 5)
        Assert.assertThat(entries.get(3).getElement(), is(6));

        // Total sum is 16 - 6 = 10.
        // First marker at: 10 * 0.9133758577858514 = 9.1337585778 -> 5
        // Order: (9, 3, 8, 7, 2, 6, 5|, 1, 4)
        Assert.assertThat(entries.get(2).getElement(), is(5));

        // Total sum is 10 - 5 = 5.
        // First marker at: 5 * 0.984841540199809 = 1.1051702225 -> 4
        // Order: (9, 3, 8, 7, 2, 6, 5, 4|, 1)
        Assert.assertThat(entries.get(1).getElement(), is(4));

        // Finally...
        Assert.assertThat(entries.get(0).getElement(), is(1));
    }

    private static class ConstantRandomNumber implements RandomProvider {
        private static final long serialVersionUID = 3019387660938987850L;
        private final MersenneTwister randomProvider;

        public ConstantRandomNumber() {
            this.randomProvider = new MersenneTwister(0);
        }

        @Override
        public int nextInt(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean nextBoolean() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int nextInt() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long nextLong() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public float nextFloat() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double nextDouble() {
            return this.randomProvider.nextDouble();
        }

        @Override
        public void nextBytes(byte[] bytes) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
