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

import java.util.List;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * An ordering is a construct to define how a list of elements should be ordered.
 * The ordering is a simple function that does a single action.
 * @param <E> The type to apply the ordering to.
 * @author gpampara
 */
public interface Ordering<E> {

    /**
     * Apply the ordering on the provided list.
     * @param elements The list to be ordered.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    public boolean order(List<Selection.Entry<E>> elements);
}
