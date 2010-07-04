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
package net.sourceforge.cilib.util.selection.recipes;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.SelectionBuilder;
import net.sourceforge.cilib.util.selection.ordering.RandomOrdering;

/**
 * Perform a random selection from the provided list of elements.
 * <p>
 * Random selection is performed by:
 * <ol>
 *   <li>A random element is selected from the provided list.</li>
 *   <li>Return the result.</li>
 * </ol>
 * @author Wiehann Matthysen
 * @param <E>
 */
public class RandomSelector<E> implements Selector<E> {
    private static final long serialVersionUID = -5099663528040315048L;

    private RandomProvider random;

    /**
     * Create a new instance.
     */
    public RandomSelector() {
        this.random = new MersenneTwister();
    }

    /**
     * Create a new instance with the provided {@code Random}.
     * @param random The {@code random} to use.
     */
    public RandomSelector(RandomProvider random) {
        this.random = random;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RandomSelector(RandomSelector copy) {
        this.random = new MersenneTwister();
    }

    @Override
    public SelectionBuilder<E> on(Iterable<? extends E> iterable) {
        return Selection.from(iterable).orderBy(new RandomOrdering<E>(random))
                .and().reverse();
    }
}
