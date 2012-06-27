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
package net.sourceforge.cilib.coevolution.selection;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.competitive.CompetitorList;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This strategy is used to select the pool of potential opponents for a competitive coevolution algorithm.
 * When opponents is selected for an {@linkplain Entity} the algorithm uses an arbitrary number of
 * {@linkplain OpponentPoolSelectionStrategy}'s to determine the potentail pool of competitors, then they are
 * selected with the {@linkplain OpponentSelectionStrategy}
 *
 */
public abstract class OpponentPoolSelectionStrategy implements Cloneable {

    public OpponentPoolSelectionStrategy() {
    }

    public OpponentPoolSelectionStrategy(OpponentPoolSelectionStrategy other) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract OpponentPoolSelectionStrategy getClone();

    /**
     * Add {@linkplain Competitor}s to the {@linkplain CoevolutionCompetitorList} pool from the sub populations
     * @param pool the pool of competitors
     * @param populations the list of sub populations
     */
    public abstract void addToCompetitorPool(CompetitorList pool, List<PopulationBasedAlgorithm> populations);
}
