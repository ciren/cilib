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
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.WeightedObject;
import net.sourceforge.cilib.util.selection.weighting.LinearWeighting;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author gpampara
 */
public class ProportionalArrangementTest {

    @Test
    public void proportionalOrdering() {
        List<Integer> elements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<WeightedObject> entries = Selection.copyOf(elements)
                .weigh(new LinearWeighting(1, 9))
                .orderBy(new ProportionalArrangement(new ConstantRandomNumber()))
                .weightedElements();

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
        Integer i = (Integer) entries.get(8).getObject();
        Assert.assertThat(i.intValue(), is(9));

        // Total sum is 45 - 9 = 36.
        // First marker at: 36 * 0.13547700503158466 = 4.87717218108 -> 3
        // Order: (9, 3|, 1, 2, 4, 5, 6, 7, 8)
        i = (Integer) entries.get(7).getObject();
        Assert.assertThat(i.intValue(), is(3));

        // Total sum is 36 - 3 = 33.
        // First marker at: 33 * 0.9057919358463374 = 29.8911338827 -> 8
        // Order: (9, 3, 8|, 1, 2, 4, 5, 6, 7)
        i = (Integer) entries.get(6).getObject();
        Assert.assertThat(i.intValue(), is(8));

        // Total sum is 33 - 8 = 25.
        // First marker at: 25 * 0.8350085784045876 = 20.87521446 -> 7
        // Order: (9, 3, 8, 7|, 1, 2, 4, 5, 6)
        i = (Integer) entries.get(5).getObject();
        Assert.assertThat(i.intValue(), is(7));

        // Total sum is 25 - 7 = 18.
        // First marker at: 18 * 0.12698681606155282 = 2.28576268908 -> 2
        // Order: (9, 3, 8, 7, 2|, 1, 4, 5, 6)
        i = (Integer) entries.get(4).getObject();
        Assert.assertThat(i.intValue(), is(2));

        // Total sum is 18 - 2 = 16.
        // First marker at: 16 * 0.9688677774872758 = 15.50188444 -> 6
        // Order: (9, 3, 8, 7, 2, 6|, 1, 4, 5)
        i = (Integer) entries.get(3).getObject();
        Assert.assertThat(i.intValue(), is(6));

        // Total sum is 16 - 6 = 10.
        // First marker at: 10 * 0.9133758577858514 = 9.1337585778 -> 5
        // Order: (9, 3, 8, 7, 2, 6, 5|, 1, 4)
        i = (Integer) entries.get(2).getObject();
        Assert.assertThat(i.intValue(), is(5));

        // Total sum is 10 - 5 = 5.
        // First marker at: 5 * 0.984841540199809 = 1.1051702225 -> 4
        // Order: (9, 3, 8, 7, 2, 6, 5, 4|, 1)
        i = (Integer) entries.get(1).getObject();
        Assert.assertThat(i.intValue(), is(4));

        // Finally...
        i = (Integer) entries.get(0).getObject();
        Assert.assertThat(i.intValue(), is(1));
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
