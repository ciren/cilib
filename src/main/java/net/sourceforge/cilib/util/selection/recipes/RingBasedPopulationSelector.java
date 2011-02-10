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

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.Arrangement;
import net.sourceforge.cilib.util.selection.arrangement.RingBasedArrangement;

/**
 * @author Wiehann Matthysen
 */
public class RingBasedPopulationSelector implements Selector<PopulationBasedAlgorithm> {

    private static final long serialVersionUID = 8899308548978334236L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<PopulationBasedAlgorithm> on(Iterable<PopulationBasedAlgorithm> iterable) {
        Arrangement ordering = new RingBasedArrangement(AbstractAlgorithm.get());
        return Selection.copyOf(iterable).orderBy(ordering);
    }
}
