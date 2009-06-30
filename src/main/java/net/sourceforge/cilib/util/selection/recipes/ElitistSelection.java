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
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.Selection.Entry;
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
public class ElitistSelection<E extends Comparable> implements SelectionRecipe<E> {
    private static final long serialVersionUID = -5432603299031620114L;

    private Comparator<Entry<E>> comparator;

    /**
     * Create a new instance with a defined comparator being {@link DefaultComparator}.
     */
    public ElitistSelection() {
        this.comparator = new DefaultComparator<E>();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     * @param comparator The comparator to set.
     */
    public ElitistSelection(Comparator<Entry<E>> comparator) {
        this.comparator = comparator;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ElitistSelection(ElitistSelection copy) {
        this.comparator = copy.comparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElitistSelection getClone() {
        return new ElitistSelection(this);
    }

    /**
     * Set the comparator to be used.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<Entry<E>> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the current comparator.
     * @return The current comparator instance.
     */
    public Comparator<Entry<E>> getComparator() {
        return this.comparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E select(List<? extends E> elements) {
        return Selection.from(elements).orderBy(new SortedOrdering<E>(this.comparator)).last().singleSelect();
    }
}
