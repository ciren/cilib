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
import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.Selection.Entry;

/**
 * Apply a sorting operation to the provided list, ordering the list naturally
 * from smallest to largest.
 * @param <E> The comparable type.
 * @author gpampara
 */
public class SortedOrdering<E extends Comparable> implements Ordering<E> {

    private Comparator<Entry<E>> comparator;

    /**
     * Create an new instance with the default ordering defined to be an
     * ordering that enforces "natural ordering".
     */
    public SortedOrdering() {
        this.comparator = new DefaultComparator<E>();
    }

    /**
     * Create a new instance with the provided {@code comparator} as the
     * comparator to use.
     * @param comparator The comparator to use.
     */
    public SortedOrdering(Comparator<Entry<E>> comparator) {
        this.comparator = comparator;
    }

    /**
     * {@inheritDoc} Sort the provided list in a natural order, based on the defined
     * {@link Comparator}.
     */
    @Override
    public boolean order(List<Selection.Entry<E>> elements) {
        Collections.sort(elements, this.comparator);
        return true;
    }
}
