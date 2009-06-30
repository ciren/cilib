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

import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.Selection.Entry;
import net.sourceforge.cilib.util.selection.ordering.DefaultComparator;
import net.sourceforge.cilib.util.selection.ordering.RandomOrdering;
import net.sourceforge.cilib.util.selection.ordering.SortedOrdering;

/**
 * A recipe for Rank based selection.
 * <p>
 * Rank based selection is performed by:
 * <ol>
 *   <li>Sorting the list of elements in a natural ordering.</li>
 *   <li>Selecting protion of the elements that are better than the majority.</li>
 *   <li>Randomizing the sub list of elements and selecting an element from the randomized list.</li>
 *   <li>Return the result.</li>
 * </ol>
 * @author Wiehann Matthysen
 * @param <E>
 */
public class RankBasedSelection<E extends Comparable> implements SelectionRecipe<E> {
    private static final long serialVersionUID = -2387196820773731607L;

    private Comparator<Entry<E>> comparator;
    private Random random;

    /**
     * Create a new instance.
     */
    public RankBasedSelection() {
        this.random = new MersenneTwister();
        this.comparator = new DefaultComparator<E>();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     * @param comparator The comparator to use.
     */
    public RankBasedSelection(Comparator<Entry<E>> comparator) {
        this.comparator = comparator;
        this.random = new MersenneTwister();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RankBasedSelection(RankBasedSelection copy) {
        this.comparator = copy.comparator;
        this.random = copy.random.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RankBasedSelection getClone() {
        return new RankBasedSelection(this);
    }

    /**
     * Set the comparator to use.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<Entry<E>> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the comparator.
     * @return The current comparator.
     */
    public Comparator<Entry<E>> getComparator() {
        return this.comparator;
    }

    /**
     * Get the current random number generator.
     * @return The current random number generator.
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Set the random number generator to use.
     * @param random The value to set.
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E select(List<? extends E> elements) {
        List<E> selection = Selection.from(elements).orderBy(new SortedOrdering<E>(this.comparator))
            .last(this.random.nextInt(elements.size())).orderBy(new RandomOrdering<E>(this.random)).last().select();
        return selection.get(0);
    }
}
