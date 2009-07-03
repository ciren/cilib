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

import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * Apply a random ordering to the provided list. This class defines that the
 * list instance will have it's internal order randomly shuffled.
 * @param <E> The comparable type.
 * @author gpampara
 */
public class RandomOrdering<E> implements Ordering<E> {

    private Random generator;

    /**
     * Create a new instance. A {@link MersenneTwister} will be set as the
     * predefined generator instance.
     */
    public RandomOrdering() {
        this.generator = new MersenneTwister();
    }

    /**
     * Create an instance with the provided {@link Random} as the generator
     * class to use.
     * @param generator The random to use.
     */
    public RandomOrdering(Random generator) {
        this.generator = generator;
    }

    /**
     * {@inheritDoc} This ordering will be a random shuffle.
     */
    @Override
    public boolean order(List<Selection.Entry<E>> elements) {
        shuffle(elements);
        return true;
    }

    /**
     * Implementation of the Fisher-Yates shuffle algorithm. This algorithm runs in O(n).
     * <p>
     * This method has been added to the implemenation due to the fact that Collections.shuffle()
     * does not perform the same operations. Collections.shuffle() does not use the current size
     * of the permutable sublist.
     *
     * @param elements The elements to shuffle.
     */
    private void shuffle(List<Selection.Entry<E>> elements) {
        int n = elements.size();

        while (n > 1) {
            int k = generator.nextInt(n); // 0 <= k < n
            n--;
            Collections.swap(elements, n, k);
        }
    }

}
