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

import com.google.common.collect.Ordering;
import java.util.Comparator;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.ReverseArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

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
 */
public class ElitistSelector<E extends Comparable> implements Selector<E> {

    private static final long serialVersionUID = -5432603299031620114L;
    private Comparator<E> comparator;

    /**
     * Create a new instance with a defined comparator being {@link DefaultComparator}.
     */
    public ElitistSelector() {
        this.comparator = Ordering.natural();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     * @param comparator The comparator to set.
     */
    public ElitistSelector(Comparator<E> comparator) {
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
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the current comparator.
     * @return The current comparator instance.
     */
    public Comparator<E> getComparator() {
        return this.comparator;
    }

    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        return Selection.copyOf(iterable).orderBy(new SortedArrangement())
                .orderBy(new ReverseArrangement());
    }
}
