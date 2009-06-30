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
package net.sourceforge.cilib.util.selection.weighing;

import java.util.List;
import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * Interface defining how weighing for selections are to operate.
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public interface Weighing<E> extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Weighing<E> getClone();

    /**
     * Perform a weighing on the provided list of entries.
     * @param elements The entries to weigh.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    public boolean weigh(List<Selection.Entry<E>> elements);

}
