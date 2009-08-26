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

import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.ordering.Ordering;
import net.sourceforge.cilib.util.selection.ordering.RingBasedOrdering;

/**
 * @author Wiehann Matthysen
 */
public class RingBasedPopulationSelection implements SelectionRecipe<PopulationBasedAlgorithm> {
    private static final long serialVersionUID = 8899308548978334236L;

    /**
     * {@inheritDoc}
     */
    @Override
    public RingBasedPopulationSelection getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopulationBasedAlgorithm select(List<? extends PopulationBasedAlgorithm> elements) {
        Ordering<PopulationBasedAlgorithm> ordering = new RingBasedOrdering<PopulationBasedAlgorithm>((PopulationBasedAlgorithm) AbstractAlgorithm.get());
        return Selection.from(elements).orderBy(ordering).first().singleSelect();
    }
}
