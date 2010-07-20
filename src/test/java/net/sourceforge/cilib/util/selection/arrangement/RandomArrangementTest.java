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
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class RandomArrangementTest {

    // This test needs to be completed.
    @Test
    public void iteration() {
        List<Integer> ints = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        RandomArrangement arrangement = new RandomArrangement(new MersenneTwister());
        
        Iterable<Integer> iterable = arrangement.arrange(ints);
        for (Integer i : iterable) {
            System.out.println(i);
        }
    }
}