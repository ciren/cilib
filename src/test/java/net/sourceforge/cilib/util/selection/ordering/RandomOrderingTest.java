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
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.SelectionSyntax;
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
        List<SelectionSyntax.Entry<Integer>> entries = Selection.from(elements).entries();
        boolean ordered = new RandomOrdering<Integer>(new ConstantRandomNumber()).order(entries);
        Assert.assertThat(ordered, is(true));

        List<Integer> otherElements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(otherElements, new ConstantRandomNumber());

        for (int i = 0; i < entries.size(); ++i) {
            Assert.assertThat(entries.get(i).getElement(), is(equalTo(otherElements.get(i))));
        }
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
