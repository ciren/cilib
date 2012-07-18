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

import java.util.Comparator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.MemoryBasedEntity;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Select the contribution based on the best position of the social best entity of the
 * {@linkplain PopulationBasedAlgorithm}.
 *
 */
public class SocialFitnessContributionSelectionStrategy implements
        ContributionSelectionStrategy {
    private static final long serialVersionUID = -2477638582277950895L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getContribution(PopulationBasedAlgorithm algorithm) {
        MemoryBasedEntity entity = (MemoryBasedEntity) Topologies.getBestEntity(algorithm.getTopology(), 
                (Comparator) new SocialBestFitnessComparator<SocialEntity>());
        return (Vector) entity.getBestPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributionSelectionStrategy getClone() {
        return this;
    }

}
