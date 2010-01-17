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
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.RandomAdaptor;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * @author gpampara
 */
public class RandomSelectionTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        RandomSelection<Integer> selection = new RandomSelection<Integer>();
        selection.select(elements);
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        RandomSelection<Integer> selection = new RandomSelection<Integer>();
        int selected = selection.select(elements);
        Assert.assertThat(selected, is(1));
    }

    @Test
    public void selectMultiple() {
        List<Integer> elements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        RandomSelection<Integer> selection = new RandomSelection<Integer>(new ConstantRandomNumber());
        int selected = selection.select(elements);

        List<Integer> otherElements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(otherElements, new RandomAdaptor(new ConstantRandomNumber()));

        int lastIndex = otherElements.size() - 1;
        Assert.assertThat(selected, is(equalTo(otherElements.get(lastIndex))));
    }

    @Test
    public void selectRandomFrom() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        int selected = Selection.from(list).random(new ConstantRandomNumber()).select(Samples.first()).performSingle();

        List<Integer> otherElements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(otherElements, new RandomAdaptor(new ConstantRandomNumber()));

        int lastIndex = otherElements.size() - 1;
        Assert.assertThat(selected, is(equalTo(otherElements.get(lastIndex))));
    }

    private class ConstantRandomNumber implements RandomProvider {

        private static final long serialVersionUID = 3019387660938987850L;

        public ConstantRandomNumber() {
        }

        @Override
        public RandomProvider getClone() {
            return this;
        }

        @Override
        public int nextInt(int n) {
            return 6;
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
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextBytes(byte[] bytes) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
