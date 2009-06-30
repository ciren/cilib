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

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * Arrange all elements as a ring and depending on the desired pivot point, return
 * an ordering that defines the list of elements that follow on from the pivot.
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public class RingBasedOrdering<E> implements Ordering<E> {

    private E marker;

    /**
     * Create a new instance. The defined marker or pivot point is {@code null}.
     */
    public RingBasedOrdering() {
        this.marker = null;
    }

    /**
     * Create a new instance with the provided {@code marker} as the pivot point.
     * @param marker The pivot point to use.
     */
    public RingBasedOrdering(E marker) {
        this.marker = marker;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The application of this ordering will result in "next in line" ordering, based
     * on the currently provided {@code marker}.
     * <p>
     * Example:<br/>
     * If the set of integers, {1, 2, 3, 4, 5, 6, 7, 8, 9}, is ordered with the marker
     * element defined to be 5, the resulting order will be: {6, 7, 8, 9, 1, 2, 3, 4, 5}
     */
    @Override
    public boolean order(List<Selection.Entry<E>> elements) {
        List<Selection.Entry<E>> tmp = new ArrayList<Selection.Entry<E>>(elements);

        int position = 0;
        for (Selection.Entry<E> entry : elements) {
            if (this.marker.equals(entry.getElement()))
                break;
            position++;
        }

        for (int i = 0; i < elements.size(); ++i) {
            elements.set(i, tmp.get((position + 1 + i) % elements.size()));
        }

        return true;
    }
}
