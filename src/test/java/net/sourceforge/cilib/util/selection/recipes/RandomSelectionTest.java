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

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.Selection;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class RandomSelectionTest {

    @Test
    public void randomSelection() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Integer selected = Selection.randomFrom(list, new ConstantRandomNumber());

        Assert.assertEquals(7, selected.intValue());
    }

    private class ConstantRandomNumber extends Random {
        private static final long serialVersionUID = 3019387660938987850L;

        public ConstantRandomNumber() {
            super(0);
        }

        @Override
        public Random getClone() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int nextInt(int n) {
            return super.nextInt(n);
        }

    }

}
