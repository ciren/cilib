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

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

/**
 * A recipe for Rank based selection.
 * <p>
 * Rank based selection is performed by:
 * <ol>
 *   <li>Sorting the list of elements in a natural ordering.</li>
 *   <li>Selecting portion of the elements that are better than the majority.</li>
 *   <li>Randomizing the sub list of elements and selecting an element from the randomized list.</li>
 *   <li>Return the result.</li>
 * </ol>
 * @author Wiehann Matthysen
 * @param <E>
 */
public class RankBasedSelector<E extends Comparable<? super E>> implements Selector<E> {

    private static final long serialVersionUID = -2387196820773731607L;
    private Comparator<E> comparator;
    private RandomProvider random;

    /**
     * Create a new instance.
     */
    public RankBasedSelector() {
        this.random = new MersenneTwister();
        this.comparator = Ordering.natural();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     * @param comparator The comparator to use.
     */
    public RankBasedSelector(Comparator<E> comparator) {
        this.comparator = comparator;
        this.random = new MersenneTwister();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RankBasedSelector(RankBasedSelector<E> copy) {
        this.comparator = copy.comparator;
        this.random = new MersenneTwister();
    }

    /**
     * Set the comparator to use.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the comparator.
     * @return The current comparator.
     */
    public Comparator<E> getComparator() {
        return this.comparator;
    }

    /**
     * Get the current random number generator.
     * @return The current random number generator.
     */
    public RandomProvider getRandom() {
        return random;
    }

    /**
     * Set the random number generator to use.
     * @param random The value to set.
     */
    public void setRandom(RandomProvider random) {
        this.random = random;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        int size = Iterables.size(iterable);
        List<E> list = Selection.copyOf(iterable).orderBy(new SortedArrangement()).select(Samples.last(random.nextInt(size)+1));
        return Selection.copyOf(list).orderBy(new RandomArrangement(random));
    }
}
