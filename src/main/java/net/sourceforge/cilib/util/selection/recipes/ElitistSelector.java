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

import java.util.Comparator;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.SelectionBuilder;
import net.sourceforge.cilib.util.selection.ordering.DefaultComparator;
import net.sourceforge.cilib.util.selection.ordering.SortedOrdering;

/**
 * A recipe for Elitist selection.
 * <p>
 * Elitist selection is performed by:
 * <ol>
 *   <li>Sorting the list of elements in a natural ordering.</li>
 *   <li>Selecting the last element from the list as it is the "best".</li>
 *   <li>Return the result.</li>
 * </ol>
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public class ElitistSelector<E extends Comparable<? super E>> implements Selector<E> {
    private static final long serialVersionUID = -5432603299031620114L;

    private Comparator<Selection.Entry<E>> comparator;

    /**
     * Create a new instance with a defined comparator being {@link DefaultComparator}.
     */
    public ElitistSelector() {
        this.comparator = new DefaultComparator<E>();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     * @param comparator The comparator to set.
     */
    public ElitistSelector(Comparator<Selection.Entry<E>> comparator) {
        this.comparator = comparator;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ElitistSelector(ElitistSelector<E> copy) {
        this.comparator = copy.comparator;
    }

    /**
     * Set the comparator to be used.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<Selection.Entry<E>> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the current comparator.
     * @return The current comparator instance.
     */
    public Comparator<Selection.Entry<E>> getComparator() {
        return this.comparator;
    }

    @Override
    public SelectionBuilder<E> on(Iterable<? extends E> iterable) {
        return Selection.from(iterable).orderBy(new SortedOrdering<E>(this.comparator)).and().reverse();
    }
}
