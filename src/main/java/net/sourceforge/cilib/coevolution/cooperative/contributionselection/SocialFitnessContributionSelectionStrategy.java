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
package net.sourceforge.cilib.coevolution.cooperative.contributionselection;

import java.util.Comparator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.MemoryBasedEntity;
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Select the contribution based on the best position of the social best entity of the
 * {@linkplain PopulationBasedAlgorithm}.
 *
 * @author leo
 */
public class SocialFitnessContributionSelectionStrategy implements
        ContributionSelectionStrategy {
    private static final long serialVersionUID = -2477638582277950895L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getContribution(PopulationBasedAlgorithm algorithm) {
        MemoryBasedEntity entity = (MemoryBasedEntity) algorithm.getTopology().getBestEntity((Comparator) new SocialBestFitnessComparator<SocialEntity>());
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
