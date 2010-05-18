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
package net.sourceforge.cilib.coevolution.cooperative.contributionselection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This inteface dictates how a participating {@linkplain PopulationBasedAlgorithm} should select its participant solution for a {@linkplain CooperativeCoevolutionAlgorithm}.
 * @author leo
 */
public interface ContributionSelectionStrategy extends Cloneable {

    /**
     * Return the relevant participant solution.
     * @param algorithm The {@linkplain PopulationBasedAlgorithm} to select the participant from.
     * @return The participating solution {@linkplain Vector}
     */
    Vector getContribution(PopulationBasedAlgorithm algorithm);

    /**
     * {@inheritDoc}
     */
    @Override
    ContributionSelectionStrategy getClone();

}
