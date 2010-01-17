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

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomAdaptor;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Selection;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * @author gpampara
 */
public class RandomOrderingTest {

    @Test
    public void randomOrdering() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Selection.Entry<Integer>> entries = Selection.from(elements).entries();
        boolean ordered = new RandomOrdering<Integer>(new ConstantRandomNumber()).order(entries);
        Assert.assertThat(ordered, is(true));

        List<Integer> otherElements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(otherElements, new RandomAdaptor(new ConstantRandomNumber()));

        for (int i = 0; i < entries.size(); ++i) {
            Assert.assertThat(entries.get(i).getElement(), is(equalTo(otherElements.get(i))));
        }
    }

    private static class ConstantRandomNumber implements RandomProvider {
        private static final long serialVersionUID = 3019387660938987850L;
        private RandomProvider randomProvider = new MersenneTwister(0);

        @Override
        public RandomProvider getClone() {
            throw new UnsupportedOperationException("Not supported yet.");
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
        public int nextInt(int n) {
            return this.randomProvider.nextInt(n);
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
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextBytes(byte[] bytes) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
