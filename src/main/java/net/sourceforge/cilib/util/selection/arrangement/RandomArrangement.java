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
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 * @author gpampara
 */
public class RandomArrangement<T> implements Arrangement<T> {

    private final RandomProvider random;

    public RandomArrangement(RandomProvider random) {
        this.random = random;
    }

    @Override
    public Iterable<T> arrange(final Iterable<T> elements) {
        final List<T> list = Lists.newArrayList(elements);
        shuffle(list);
        return list;
    }

    /**
     * Implementation of the Fisher-Yates shuffle algorithm. This algorithm runs in O(n).
     * <p>
     * This method has been added to the implementation due to the fact that Collections.shuffle()
     * does not perform the same operation efficiently. Collections.shuffle() <b>does not</b>
     * use the current size of the permutation sublist.
     *
     * @param elements The elements to shuffle.
     */
    private <E> void shuffle(List<E> elements) {
        int n = elements.size();

        while (n > 1) {
            int k = random.nextInt(n); // 0 <= k < n
            n--;
            Collections.swap(elements, n, k);
        }
    }
}
