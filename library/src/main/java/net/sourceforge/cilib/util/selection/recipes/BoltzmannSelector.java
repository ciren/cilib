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

import java.util.Arrays;
import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.comparator.BoltzmannComparator;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;

public class BoltzmannSelector<E extends Entity> implements Selector<E> {
    
    private BoltzmannComparator comparator;
    
    public BoltzmannSelector() {
        this.comparator = new BoltzmannComparator();
    }

    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        Iterator<E> iter = iterable.iterator();
        E item1 = iter.next();
        E item2 = iter.next();        
        
        if (comparator.compare(item1, item2) < 0) {
            return Selection.copyOf(Arrays.asList(item2));
        }
        
        return Selection.copyOf(Arrays.asList(item1));
    }

    public void setComparator(BoltzmannComparator comparator) {
        this.comparator = comparator;
    }
}
