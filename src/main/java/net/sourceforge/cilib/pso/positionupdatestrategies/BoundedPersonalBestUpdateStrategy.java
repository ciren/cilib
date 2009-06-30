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
package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.type.types.Types;

/**
 * Update the personal best of the particle, if it is a valid update. Valid updates are
 * defined to be only within the problem search space. Any particle drifting into an
 * infeasible part of the search space will be allowed to do so, however, any solutions
 * found will not allowed to become personal best positions.
 *
 * @author gpampara
 */
public class BoundedPersonalBestUpdateStrategy extends StandardPersonalBestUpdateStrategy {
    private static final long serialVersionUID = -3574938411781908840L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * Update personal best if and only if the particle is within the bounds of the
     * search space / problem.
     * @param particle The particle to update.
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
