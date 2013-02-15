/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.Types;

/**
 * <p> Implementation of {@link GuideUpdateStrategy} where a particle's guide
 * can get updated if the new guide is not dominated by the current guide, i.e.
 * both of the guides are non-dominated. If both guides are non- dominated the
 * new guide is selected. </p>
 *
 * @author Marde Greeff
 *
 */
public class BoundedRelaxedNonDominatedPersonalBestUpdateStrategy extends RelaxedNonDominatedPersonalBestUpdateStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * Updates the guide. If the new guide dominates the old guide, the new
     * guide is selected. However, if both guides are non-dominated, one of the
     * guides is randomly selected.
     * @param particle The particle who's guide is to be updated.
     * @param guideType If the local or global guide should be updated.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        if (!Types.isInsideBounds(particle.getPosition())) {
            particle.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
            return;
        }

        super.updatePersonalBest(particle);
    }
}
