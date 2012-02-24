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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.moo.archive.Archive;

/**
 * This strategy responds to a change by removing all solutions from the archive.
 * This may be required when dealing with dynamic MOO functions where the POF
 * changes from concave to convex or from convex to concave.
 *
 * @author Marde Greeff
 *
 */
public class ArchiveAlwaysClearingResponseStrategy<E extends PopulationBasedAlgorithm> extends
        EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm>{

    private static final long serialVersionUID = -3042868407937040175L;

    /**
     * @{@inheritDoc}
     */
    @Override
    public EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> getClone() {
        return this;
    }

    /**
     * Responds to a change by removing all solutions from the archive.
     * @param algorithm The algorithm to perform the response on.
     */
    @Override
    protected void performReaction(PopulationBasedAlgorithm algorithm) {
        //clearing all solutions from the archive
    	Archive.Provider.get().clear();
    }
}

