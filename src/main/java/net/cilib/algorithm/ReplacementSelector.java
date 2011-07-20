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
package net.cilib.algorithm;

import com.google.inject.Inject;
import fj.data.Array;
import fj.data.List;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.HasFitness;

/**
 *
 * @author gpampara
 */
public class ReplacementSelector implements Selector {

    private final FitnessComparator comparator;

    @Inject
    public ReplacementSelector(FitnessComparator comparator) {
        this.comparator = comparator;
    }

    @Override
    public <A extends HasFitness> A select(A first, A... rest) {
        return select(List.cons(first, Array.array(rest).toList()));
    }

    @Override
    public <A extends HasFitness> A select(List<A> elements) {
        A selected = null; // This should really be: Entity selected = Entity.dummy(); // or some name indicating that it's a temporary value
        for (A candidate : elements) {
            selected = (selected == null) ? candidate : comparator.moreFit(selected, candidate);
        }
        return selected;
    }
}
